package com.r0ck1n70sh.csvanalyzer.pipeline;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.r0ck1n70sh.csvanalyzer.entities.ChatMessageEntity;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvColumn;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import com.r0ck1n70sh.csvanalyzer.enums.ChatStatus;
import com.r0ck1n70sh.csvanalyzer.enums.ColumnType;
import com.r0ck1n70sh.csvanalyzer.openai.OpenAiServiceInstance;
import com.r0ck1n70sh.csvanalyzer.openai.functions.ChatFunctionExecutors;
import com.r0ck1n70sh.csvanalyzer.openai.functions.ColumnNameArgs;
import com.r0ck1n70sh.csvanalyzer.openai.functions.JsonObjectStrArgs;
import com.r0ck1n70sh.csvanalyzer.openai.functions.NoArgs;
import com.r0ck1n70sh.csvanalyzer.utils.JsonUtils;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import lombok.NonNull;

import java.util.*;

public class CsvProcessorComponent implements Component {
    private final RawCsvMeta csv;

    private final List<ChatMessage> messages;

    private final FunctionExecutor executor;

    public CsvProcessorComponent(@NonNull RawCsvMeta csv) {
        this.csv = csv;
        this.messages = new ArrayList<>();
        this.executor = new FunctionExecutor(getChatFunctions());
    }

    @Override
    public ComponentResponse conduct() {
        ComponentResponse response = new ComponentResponse();

        Map<String, String[]> columnTypeMap = getColumnType();
        response.setColumnNameTypeMap(columnTypeMap);

        response.setStatus(ChatStatus.CSV_PROCESSED);

        List<ChatMessageEntity> messageEntities =
                messages.stream().map(ChatMessageEntity::new).toList();

        response.setIntermediates(messageEntities);

        return response;
    }

    private String getInitialSystemPromptMessage() {
        return String.format("""
                You are given a CSV File, with Following column names.
                %s
                                
                Classify these columns into following types:
                    1. Categorical
                    2. Numerical
                    3. Temporal,
                and identified as "Numerical", then identify it's units also.
                                
                Give output as single valid JSON object, for ex.
                {
                    "column_name_1": {
                        "type": "" // can be Categorical, Numerical, Temporal
                        "unit": "" // only applicable for "Numerical" type. Can be number, ratio, percentage or currency
                    },
                    "column_name_2": {
                        "type": "" // can be Categorical, Numerical, Temporal
                        "unit": "" // only applicable for "Numerical" type. Can be number, ratio, percentage or currency
                    },
                }
                                
                Note: output should be only JSON object, not other text allowed
                Hint: use function call, to get sample data
                """, formattedColumns());
    }

    private Map<String, String[]> getColumnType() {
        ChatMessage systemPrompt = new ChatMessage(
                ChatMessageRole.SYSTEM.value(),
                getInitialSystemPromptMessage());

        messages.add(systemPrompt);
        ChatMessage response;
        String content;
        ChatFunctionCall functionCall;

        for (int tries = 0; tries < csv.getColumns().size() + 10; tries++) {
            try {
                Thread.sleep(20 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            response = getOpenAiResponse();
            messages.add(response);

            content = response.getContent();
            functionCall = response.getFunctionCall();

            if (functionCall != null) {
                Optional<ChatMessage> optional = executor.executeAndConvertToMessageSafely(functionCall);

                if (optional.isEmpty()) {
                    String functionName = functionCall.getName();
                    ChatMessage errorFunctionMessage = buildErrorFunctionMessage(functionName);
                    messages.add(errorFunctionMessage);

                } else {
                    messages.add(optional.get());
                }
            }

            if (content != null) {
                content = content.replace("```json", "");
                content = content.replace("```", "");

                boolean isValidJson = JsonUtils.isValidJsonObjectString(content);
                if (!isValidJson) {
                    ChatMessage errorInvalidJsonMessage = buildErrorInvalidJsonMessage(content);
                    messages.add(errorInvalidJsonMessage);
                    continue;
                }

                List<ChatMessage> errorPromptMessages = buildErrorPromptMessage(content);

                if (errorPromptMessages.isEmpty()) {
                    return parseColumnTypes(content);
                }

                messages.addAll(errorPromptMessages);
            }
        }

        return null;
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

    private List<ChatFunction> getChatFunctions() {
        ChatFunction[] chatFunctions = {
                ChatFunctionExecutors.buildChatFunction(
                        "is_valid_json",
                        "Verify whether string/text is a valid JSON",
                        JsonObjectStrArgs.class,
                        args -> ChatFunctionExecutors.isValidJsonObjectStr(args.jsonStr)),

                ChatFunctionExecutors.buildChatFunction(
                        "get_column_types",
                        "Get all valid column type ex. Categorical",
                        NoArgs.class,
                        args -> ChatFunctionExecutors.getColumnTypes()
                ),

                ChatFunctionExecutors.buildChatFunction(
                        "get_column_names",
                        "Get all valid column name for csv",
                        NoArgs.class,
                        args -> ChatFunctionExecutors.getColumnNames(csv)
                ),

                ChatFunctionExecutors.buildChatFunction(
                        "get_sample_data",
                        "Get data points for a particular column name ex. Year",
                        ColumnNameArgs.class,
                        args -> ChatFunctionExecutors.getSampleData(args.columnName, 5, csv)
                )
        };

        return List.of(chatFunctions);
    }

    private ChatMessage buildErrorFunctionMessage(String name) {
        String message;

        switch (name) {
            case "is_valid_json":
                message = """
                        One string/text argument required by function "is_valid_json"
                        """;
                break;
            case "get_column_types":
                message = """
                        One string/text argument required by function "get_column_types"
                        """;
                break;
            case "get_column_names":
                message = """
                        No arguments, required by function "get_column_names"
                        """;
                break;
            case "get_sample_data":
            default:
                message = """
                        Incorrect arguments in function call, try again
                        all column names of csv, are valid argument, for this function
                        call "get_column_names", for valid column names.                  
                        """;
        }

        return new ChatMessage(ChatMessageRole.FUNCTION.value(), message, name);
    }

    private ChatMessage buildErrorInvalidJsonMessage(String content) {
        String message = """
                response is invalid json object,
                call "is_valid_json"
                """;

        return new ChatMessage(ChatMessageRole.SYSTEM.value(), message);
    }

    private List<ChatMessage> buildErrorPromptMessage(String content) {
        List<ChatMessage> errorMessages = new ArrayList<>();

        JsonObject jsonObject = JsonUtils.parseJsonStrAsJsonObject(content);
        Map<String, JsonElement> map = jsonObject.asMap();
        Set<String> parsedColumns = map.keySet();

        Set<String> columns = new HashSet<>(getColumnNames());

        List<String> missing = new ArrayList<>();
        for (String column : columns) {
            if (parsedColumns.contains(column)) continue;
            missing.add(column);
        }

        if (!missing.isEmpty()) {
            String message = String.format("""
                            Following columns are missing, in json output:
                            %s
                            call "get_column_names", for all columns
                            """,
                    formattedColumns(missing));

            ChatMessage chatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), message);
            errorMessages.add(chatMessage);
        }

        List<String> extra = new ArrayList<>();
        for (String column : parsedColumns) {
            if (columns.contains(column)) continue;
            extra.add(column);
        }

        if (!extra.isEmpty()) {
            String message = """
                    Following columns are invalid, in json output,
                    call "get_column_names", for all columns
                    """;

            ChatMessage chatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), message);
            errorMessages.add(chatMessage);
        }

        for (Map.Entry<String, JsonElement> entry : map.entrySet()) {
            String columnName = entry.getKey();
            JsonObject json = entry.getValue().getAsJsonObject();

            if (!columns.contains(columnName)) continue;

            if (!json.has("type")) {
                String message = String.format("""
                                Property "type" not present for column: %s
                                """
                        , columnName);
                ChatMessage chatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), message);
                errorMessages.add(chatMessage);
                continue;
            }

            String type = json.get("type").getAsString();
            ColumnType columnType = ColumnType.fromString(type);

            if (columnType == null) {
                String message = String.format("""
                                Property "type": %s, for column: %s, is invalid
                                call "get_column_types", for valid column types
                                """,
                        type, columnName);
                ChatMessage chatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), message);
                errorMessages.add(chatMessage);
            }

            if (columnType == ColumnType.NUMERICAL && !json.has("unit")) {
                String message = String.format("""
                                Property "type": "NUMERICAL", for column: %s,
                                doesn't have Property "unit" associated with it.
                                """,
                        columnName);
                ChatMessage chatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), message);
                errorMessages.add(chatMessage);
            }
        }

        return errorMessages;
    }

    private Map<String, String[]> parseColumnTypes(String str) {
        JsonObject json = JsonUtils.parseJsonStrAsJsonObject(str);
        Map<String, JsonElement> map = json.asMap();

        Map<String, String[]> columnTypes = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : map.entrySet()) {
            String columnName = entry.getKey();
            JsonObject jsonObject = entry.getValue().getAsJsonObject();

            String[] typeAndUnit = new String[2];
            typeAndUnit[0] = jsonObject.get("type").getAsString();
            typeAndUnit[1] = jsonObject.has("unit") ? jsonObject.get("unit").getAsString() : null;

            columnTypes.put(columnName, typeAndUnit);
        }

        return columnTypes;
    }

    private String formattedColumns() {
        return formattedColumns(getColumnNames());
    }

    private String formattedColumns(List<String> columnNames) {
        String res = "";
        for (int i = 0; i < columnNames.size(); ++i) {
            String columnName = columnNames.get(i);
            res += String.format("%d. %s\n", i, columnName);
        }

        return res;
    }

    private List<String> getColumnNames() {
        List<RawCsvColumn> columns = csv.getColumns();
        return columns.stream().map(RawCsvColumn::getName).toList();
    }
}
