package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.entities.ChatMessageEntity;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import com.r0ck1n70sh.csvanalyzer.enums.ChatStatus;
import com.r0ck1n70sh.csvanalyzer.graphs.Graph;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ComponentResponse {
    private List<ChatMessageEntity> intermediates;
    private ChatMessageEntity response;
    private RawCsvMeta csv;
    private ChatStatus status;
    private Graph graph;
    private Map<String, String[]> columnNameTypeMap;

    @Override
    public String toString() {
        return String.format(
                """
                        {
                            "intermediates: %s,
                            "response": %s,
                            "csv": %s,
                            "status": %s,
                            "graph": %s
                        }
                        """,
                intermediates, response, csv, status, graph
        );
    }
}
