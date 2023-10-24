package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.crud.ChatSessionCrud;
import com.r0ck1n70sh.csvanalyzer.entities.ChatMessageEntity;
import com.r0ck1n70sh.csvanalyzer.entities.ChatSession;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvColumn;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import com.r0ck1n70sh.csvanalyzer.enums.ChatStatus;
import com.r0ck1n70sh.csvanalyzer.enums.ColumnType;
import com.r0ck1n70sh.csvanalyzer.graphs.Graph;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class PipelineConductor {
    @NonNull
    private final ChatSession chatSession;

    @NonNull
    private final ComponentResponse componentResponse;

    public PipelineResponse updateSessionAndRespond() {
        updateSession();
        return respond();
    }

    private void updateSession() {
        updateColumns();
        updateSessionStatus();
        UpdateSessionCsv();
        addChatMessages();

        ChatSessionCrud.save(chatSession);
    }

    private void updateColumns() {
        Map<String, String[]> columnNameTypeMap = componentResponse.getColumnNameTypeMap();
        if (columnNameTypeMap == null) return;

        RawCsvMeta csv = chatSession.getCsv();
        if (csv == null) return;

        List<RawCsvColumn> columns = csv.getColumns();

        for (Map.Entry<String, String[]> entry : columnNameTypeMap.entrySet()) {
            String columnName = entry.getKey();
            String[] value = entry.getValue();

            ColumnType type = ColumnType.fromString(value[0]);
            String unit = value[1];

            RawCsvColumn column =
                    columns.stream()
                            .filter(c -> c.getName().equals(columnName))
                            .findFirst()
                            .orElse(null);

            if (column == null) continue;

            column.setType(type);
            column.setUnit(unit);
        }
    }

    private void updateSessionStatus() {
        ChatStatus status = componentResponse.getStatus();
        if (status == null) return;

        chatSession.setStatus(status);
    }

    private void UpdateSessionCsv() {
        RawCsvMeta csv = componentResponse.getCsv();
        if (csv == null) return;

        chatSession.setCsv(csv);
    }

    private void addChatMessages() {
        List<ChatMessageEntity> messages = new ArrayList<>();

        List<ChatMessageEntity> intermediates = componentResponse.getIntermediates();
        if (intermediates != null) {
            messages.addAll(intermediates);
        }

        ChatMessageEntity response = componentResponse.getResponse();
        if (response != null) {
            messages.add(response);
        }

        chatSession.getMessages().addAll(messages);
    }

    private PipelineResponse respond() {
        PipelineResponse pipelineResponse = new PipelineResponse();

        ChatStatus status = chatSession.getStatus();
        pipelineResponse.setStatus(status);

        ChatMessageEntity response = componentResponse.getResponse();
        if (response != null) {
            pipelineResponse.setMessage(response.getContent());
        }

        Graph graph = componentResponse.getGraph();
        if (graph != null) {
            pipelineResponse.setGraph(componentResponse.getGraph());
        }

        return pipelineResponse;
    }
}
