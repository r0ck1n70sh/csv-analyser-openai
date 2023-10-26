package com.r0ck1n70sh.csvanalyzer.graphs;

import com.r0ck1n70sh.csvanalyzer.entities.RawCsvColumn;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvDataPoint;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import com.r0ck1n70sh.csvanalyzer.enums.ColumnType;
import com.r0ck1n70sh.csvanalyzer.enums.GraphType;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class GraphGenerator {
    private final RawCsvMeta csv;

    public Graph generate(@NonNull List<String> columnNames, @NonNull GraphType type) {
        Graph graph = new Graph();
        graph.setType(type);

        List<Map<String, String>> dataPoints = new ArrayList<>(csv.getSize());
        Map<String, ColumnType> columnTypeMap = new HashMap<>();

        for (int idx = 0; idx < csv.getSize(); idx++) {
            dataPoints.add(idx, new HashMap<>());
        }

        for (RawCsvColumn column : csv.getColumns()) {
            String name = column.getName();
            if (!columnNames.contains(name)) continue;

            columnTypeMap.put(name, column.getType());

            for (RawCsvDataPoint dataPoint : column.getDataPoints()) {
                int idx = dataPoint.getIndex();
                String data = dataPoint.getData();
                Map<String, String> map = dataPoints.get(idx);

                map.put(name, data);
            }
        }

        graph.setDataPoints(dataPoints);
        graph.setColumnTypeMap(columnTypeMap);

        return graph;
    }
}
