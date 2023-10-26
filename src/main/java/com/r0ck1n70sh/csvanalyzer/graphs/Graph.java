package com.r0ck1n70sh.csvanalyzer.graphs;

import com.google.gson.JsonArray;
import com.r0ck1n70sh.csvanalyzer.enums.ColumnType;
import com.r0ck1n70sh.csvanalyzer.enums.GraphType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Graph {
    private GraphType type;
    private List<Map<String, String>> dataPoints;
    private Map<String, ColumnType> columnTypeMap;

    @Override
    public String toString() {
        return String.format("""
                        {
                            "type": %s,
                            "dataPoints": %s,
                            "columnTypeMap": %s
                        }
                        """,
                type, dataPoints, columnTypeMap
        );
    }
}
