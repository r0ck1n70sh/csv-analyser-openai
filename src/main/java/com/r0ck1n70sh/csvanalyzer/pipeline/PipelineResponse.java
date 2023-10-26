package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.enums.ChatStatus;
import com.r0ck1n70sh.csvanalyzer.graphs.Graph;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PipelineResponse {
    private List<Graph> graphs;
    private String message;
    private ChatStatus status;

    @Override
    public String toString() {
        return String.format(
                """
                        {
                            "graphs": %s,
                            "message": %s,
                            "status": %s
                        }
                        """,
                graphs, message, status
        );
    }
}
