package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.enums.ChatStatus;
import com.r0ck1n70sh.csvanalyzer.graphs.Graph;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PipelineResponse {
    private Graph graph;
    private String message;
    private ChatStatus status;

    @Override
    public String toString() {
        return String.format(
                """
                        {
                            "graph": %s,
                            "message": %s,
                            "status": %s
                        }
                        """,
                graph, message, status
        );
    }
}
