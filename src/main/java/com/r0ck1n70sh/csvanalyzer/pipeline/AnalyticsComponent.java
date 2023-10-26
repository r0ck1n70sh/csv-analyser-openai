package com.r0ck1n70sh.csvanalyzer.pipeline;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.r0ck1n70sh.csvanalyzer.entities.ChatMessageEntity;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvColumn;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import com.r0ck1n70sh.csvanalyzer.enums.ChatStatus;
import com.r0ck1n70sh.csvanalyzer.enums.GraphType;
import com.r0ck1n70sh.csvanalyzer.graphs.Graph;
import com.r0ck1n70sh.csvanalyzer.graphs.GraphGenerator;
import com.r0ck1n70sh.csvanalyzer.openai.OpenAiServiceInstance;
import com.r0ck1n70sh.csvanalyzer.openai.functions.*;
import com.r0ck1n70sh.csvanalyzer.utils.JsonUtils;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import lombok.NonNull;

import java.util.*;

public class AnalyticsComponent implements Component {
    private final String userPrompt;
    private final List<ChatMessage> messages;
    private final RawCsvMeta csv;
    private final FunctionExecutor executor;

    public AnalyticsComponent(@NonNull String userPrompt,
                              @NonNull List<ChatMessageEntity> messages,
                              @NonNull RawCsvMeta csv) {
        this.userPrompt = userPrompt;
        this.csv = csv;
        this.messages = new ArrayList<>();
        this.executor = new FunctionExecutor(getChatFunctions());

        this.messages.addAll(messages.stream().map(ChatMessageEntity::toMessage).toList());
    }

    @Override
    public ComponentResponse conduct() {
        ComponentResponse response = new ComponentResponse();

        Map<GraphType, List<String>> suggestedGraphs = getSuggestedGraphs();

        if (suggestedGraphs != null) {
            response.setStatus(ChatStatus.ANALYTICS_COMPLETED);
            List<Graph> graphs = generateGraphs(suggestedGraphs);
            response.setGraphs(graphs);
        } else {
            response.setStatus(ChatStatus.INVALID);
        }


        List<ChatMessageEntity> intermediates =
                messages.stream()
                        .map(ChatMessageEntity::new)
                        .toList();

        response.setIntermediates(intermediates);

        return response;
    }

    private Map<GraphType, List<String>> getSuggestedGraphs() {
        if (userPrompt == null) {
            return null;
        }

        ChatMessage userPromptMessage = buildInitialUserPromptMessage();
        messages.add(userPromptMessage);

        ChatMessage systemPromptMessage = buildInitialSystemPromptMessage();
        messages.add(systemPromptMessage);

        ChatMessage response;
        String content;
        ChatFunctionCall functionCall;

        List<String> columnsNames = getColumnNames();

        for (int tries = 0; tries < csv.getColumns().size() + 10; tries++) {
            try {
                Thread.sleep(20 * 1000);
            } catch (InterruptedException e) {

            }

            Map<GraphType, List<String>> suggested = new HashMap<>();

            response = getOpenAiResponse();
            messages.add(response);

            content = response.getContent();
            functionCall = response.getFunctionCall();

            if (functionCall != null) {
                String name = functionCall.getName();

                Optional<ChatMessage> optional = executor.executeAndConvertToMessageSafely(functionCall);
                if (optional.isEmpty()) {
                    ChatMessage message = buildErrorMessageFunctionCall(name);
                    messages.add(message);
                } else {
                    messages.add(optional.get());
                }
            }

            if (content != null) {
                boolean isValidJsonArray = JsonUtils.isValidJsonArrayString(content);

                if (!isValidJsonArray) {
                    ChatMessage chatMessage = new ChatMessage(
                            ChatMessageRole.SYSTEM.value(),
                            """
                                    invalid json array, call is_valid_json_array to verify.                 
                                    """
                    );
                    messages.add(chatMessage);
                    continue;
                }

                JsonArray jsonArray = JsonUtils.parseJsonStrAsJsonArray(content);
                if (jsonArray.size() > 2) {
                    ChatMessage chatMessage = new ChatMessage(
                            ChatMessageRole.SYSTEM.value(),
                            """
                                    maximum json objects allowed in json array is 2
                                    """);
                    messages.add(chatMessage);
                }

                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonElement jsonElem = jsonArray.get(i);

                    if (!JsonUtils.isValidJsonObjectElem(jsonElem)) {
                        ChatMessage message = new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                String.format("""
                                        invalid json, at index %d,
                                        call "is_valid_json_object" to verify.
                                        """, i
                                )
                        );
                        messages.add(message);
                        continue;
                    }

                    JsonObject jsonObject = jsonElem.getAsJsonObject();

                    GraphType graphType =
                            jsonObject.has("type")
                                    ? GraphType.fromString(jsonObject.get("type").getAsString())
                                    : null;

                    List<String> columns = null;

                    if (graphType == null) {
                        ChatMessage message = new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                String.format("""
                                        either property "type" not present, or invalid at index %d,
                                        call "get_graph_types" for valid types.
                                        """, i)
                        );
                        messages.add(message);
                    }

                    if (jsonObject.has("columns")) {
                        String columnArrStr = jsonObject.get("columns").toString();

                        if (!JsonUtils.isValidJsonArrayString(columnArrStr)) {
                            ChatMessage message = new ChatMessage(
                                    ChatMessageRole.SYSTEM.value(),
                                    String.format("""
                                            invalid json arr at property "columns", index %d,
                                            call "is_valid_json_array" to verify
                                            """, i)
                            );
                            messages.add(message);
                        }

                        JsonArray columnArr = jsonObject.get("columns").getAsJsonArray();
                        List<String> columnNames = columnArr.asList().stream().map(JsonElement::getAsString).toList();

                        List<String> invalidColumnNames =
                                columnNames.stream().filter(c -> !columnsNames.contains(c)).toList();

                        if (invalidColumnNames.isEmpty()) {
                            columns = columnNames;
                        } else {
                            ChatMessage message = new ChatMessage(
                                    ChatMessageRole.SYSTEM.value(),
                                    String.format("""
                                            invalid column names, at property "columns", index %d,
                                            call "get_column_names", for valid column names.
                                            """, i)
                            );
                            messages.add(message);
                        }

                    } else {
                        ChatMessage message = new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                String.format("""
                                        property "column" not present, at index %d
                                        """, i)
                        );
                        messages.add(message);
                    }

                    if (graphType != null && columns != null) {
                        if (!ChatFunctionExecutors.allPossibleGraphType(columnsNames, csv).contains(graphType.name)) {
                            ChatMessage message = new ChatMessage(
                                    ChatMessageRole.SYSTEM.value(),
                                    String.format("""
                                            invalid type predicted for columns, at index %d
                                            call "get_possible_graph_types", for valid graph types
                                            """, i)
                            );
                            messages.add(message);
                            continue;
                        }

                        suggested.put(graphType, columns);
                    }
                }

                if (suggested.size() == jsonArray.size()) {
                    return suggested;
                }
            }
        }

        return null;
    }

    private List<Graph> generateGraphs(Map<GraphType, List<String>> graphTypeColumnListMap) {
        GraphGenerator generator = new GraphGenerator(csv);
        List<Graph> graphs = new ArrayList<>();

        for (Map.Entry<GraphType, List<String>> entry : graphTypeColumnListMap.entrySet()) {
            GraphType type = entry.getKey();
            List<String> columnNames = entry.getValue();

            Graph graph = generator.generate(columnNames, type);
            graphs.add(graph);
        }

        return graphs;
    }

    private ChatMessage buildInitialUserPromptMessage() {
        return new ChatMessage(ChatMessageRole.USER.value(), userPrompt);
    }

    private ChatMessage buildInitialSystemPromptMessage() {
        return new ChatMessage(
                ChatMessageRole.SYSTEM.value(),
                """
                        Based on the given user prompt, suggest graphs (maximum 2) to be rendered.
                        Given output only as valid JSON Array format, ex.
                        [
                            {
                                "type": "line_chart"
                                "columns": ["Year", "Revenue"]
                            },
                            {
                                "type": "pie_chart"
                                "columns": ["Item", "Revenue"]
                            }
                        ]
                        Hint: graph types can be determined, by knowing column type
                        Use appropriate functions given to know more about graph, columns.
                        """
        );
    }

    private List<ChatFunction> getChatFunctions() {
        ChatFunction[] functions = {
                ChatFunctionExecutors.buildChatFunction(
                        "get_possible_graph_types",
                        "Get graph types for any no. of column names",
                        ColumnNamesArgs.class,
                        (args) -> ChatFunctionExecutors.allPossibleGraphType(args.columnNames, csv)
                ),

                ChatFunctionExecutors.buildChatFunction(
                        "get_column_type",
                        "Get column type for any column name",
                        ColumnNameArgs.class,
                        (args) -> ChatFunctionExecutors.getColumnType(args.columnName, csv)
                ),

                ChatFunctionExecutors.buildChatFunction(
                        "is_valid_json_object",
                        "Verify whether a string is valid JSON object",
                        JsonObjectStrArgs.class,
                        (args) -> ChatFunctionExecutors.isValidJsonObjectStr(args.jsonStr)
                ),

                ChatFunctionExecutors.buildChatFunction(
                        "is_valid_json_array",
                        "Verify whether a string is valid JSON array",
                        JsonObjectStrArgs.class,
                        (args) -> ChatFunctionExecutors.isValidJsonArrayStr(args.jsonStr)
                ),

                ChatFunctionExecutors.buildChatFunction(
                        "get_graph_types",
                        "Get all valid graph types",
                        NoArgs.class,
                        (args) -> ChatFunctionExecutors.getGraphTypes()
                ),

                ChatFunctionExecutors.buildChatFunction(
                        "get_column_types",
                        "Get all valid column types",
                        NoArgs.class,
                        (args) -> ChatFunctionExecutors.getColumnTypes()
                ),

                ChatFunctionExecutors.buildChatFunction(
                        "get_column_names",
                        "Get all valid column names",
                        NoArgs.class,
                        (args) -> ChatFunctionExecutors.getColumnName(csv)
                )
        };

        return List.of(functions);
    }

    private ChatMessage getOpenAiResponse() {
        ChatCompletionRequest request = buildChatCompletionRequest();

        OpenAiService service = OpenAiServiceInstance.getInstance();
        return service.createChatCompletion(request).getChoices().get(0).getMessage();
    }

    private ChatCompletionRequest buildChatCompletionRequest() {
        return ChatCompletionRequest.builder()
                .model(OpenAiServiceInstance.OPEN_AI_MODEL)
                .messages(messages)
                .functions(executor.getFunctions())
                .functionCall(ChatCompletionRequest.ChatCompletionRequestFunctionCall.of("auto"))
                .n(1)
                .maxTokens(2048)
                .build();
    }

    private ChatMessage buildErrorMessageFunctionCall(String name) {
        String content;
        switch (name) {
            case "get_possible_graph_types":
                content = """
                        invalid column names list,
                        call function "get_column_names", for valid column name
                        """;
                break;

            case "get_column_type":
                content = """
                        invalid column name,
                        call function "get_column_names", for valid column name
                        """;
                break;

            case "is_valid_json_object":
            case "is_valid_json_array":
                content = """
                        pass string, to check whether it's valid json object/array
                        """;
                break;
            case "get_graph_types":
            case "get_column_types":
            case "get_column_names":
            default:
                content = "no argument required by this function";
        }

        return new ChatMessage(ChatMessageRole.FUNCTION.value(), content, name);
    }

    private List<String> getColumnNames() {
        return csv.getColumns().stream().map(RawCsvColumn::getName).toList();
    }
}
