package com.r0ck1n70sh.csvanalyzer.openai.functions;

import com.r0ck1n70sh.csvanalyzer.entities.RawCsvColumn;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvDataPoint;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import com.r0ck1n70sh.csvanalyzer.enums.ColumnType;
import com.r0ck1n70sh.csvanalyzer.enums.GraphType;
import com.r0ck1n70sh.csvanalyzer.utils.JsonUtils;
import com.theokanning.openai.completion.chat.ChatFunction;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ChatFunctionExecutors {
    public static <T> ChatFunction buildChatFunction(String name,
                                                     String description,
                                                     Class<T> requestCls,
                                                     Function<T, Object> executor) {
        return ChatFunction.builder()
                .name(name)
                .description(description)
                .executor(requestCls, executor)
                .build();
    }

    public static List<String> getSampleData(String columnName, int size, RawCsvMeta rawCsv) {
        List<RawCsvColumn> columns = rawCsv.getColumns();
        RawCsvColumn column =
                columns.stream()
                        .filter(c -> c.getName().equals(columnName))
                        .findFirst()
                        .orElse(null);

        if (column == null) {
            return new ArrayList<>();
        }

        return column
                .getDataPoints()
                .stream()
                .map(RawCsvDataPoint::getData)
                .limit(size)
                .toList();
    }

    public static List<String> getColumnName(RawCsvMeta rawCsv) {
        return rawCsv.getColumns().stream().map(RawCsvColumn::getName).toList();
    }

    public static boolean isValidJsonObjectStr(String jsonStr) {
        return JsonUtils.isValidJsonObjectString(jsonStr);
    }

    public static boolean isValidJsonArrayStr(String jsonStr) {
        return JsonUtils.isValidJsonArrayString(jsonStr);
    }

    public static List<ColumnType> getColumnTypes() {
        return List.of(ColumnType.values());
    }

    public static List<String> getColumnNames(RawCsvMeta csv) {
        if (csv == null) return new ArrayList<>();

        List<RawCsvColumn> columns = csv.getColumns();
        if (columns == null) return new ArrayList<>();

        return columns.stream().map(RawCsvColumn::getName).toList();
    }

    public static String getColumnType(@NonNull String columnName, @NonNull RawCsvMeta csv) {
        List<RawCsvColumn> columns = csv.getColumns();
        if (columns == null) return null;

        return columns.stream()
                .filter(column -> column.getName().equals(columnName))
                .map(column -> column.getType().name)
                .findFirst()
                .orElse(null);
    }

    public static List<String> getGraphTypes() {
        return Stream.of(GraphType.values()).map(graphType -> graphType.name).toList();
    }

    public static List<String> allPossibleGraphType(@NonNull List<String> columnNames, @NonNull RawCsvMeta csv) {
        Map<ColumnType, Integer> typeCountMap = new HashMap<>();
        Map<String, Integer> unitCountMap = new HashMap<>();

        for (String columnName : columnNames) {
            RawCsvColumn column =
                    csv.getColumns()
                            .stream()
                            .filter(c -> c.getName().equals(columnName))
                            .findFirst()
                            .orElse(null);

            if (column == null) {
                throw new IllegalArgumentException("invalid column name");
            }

            ColumnType type = column.getType();
            typeCountMap.put(type, typeCountMap.getOrDefault(type, 0) + 1);

            String unit = column.getUnit();
            if (unit == null) continue;

            unitCountMap.put(unit, unitCountMap.getOrDefault(unit, 0) + 1);
        }

        List<String> graphs = new ArrayList<>();
        int numerical = typeCountMap.getOrDefault(ColumnType.NUMERICAL, 0);
        int categorical = typeCountMap.getOrDefault(ColumnType.CATEGORICAL, 0);
        int temporal = typeCountMap.getOrDefault(ColumnType.TEMPORAL, 0);

        if (temporal == 1 && categorical == 0 && numerical > 0 && unitCountMap.size() == 1) {
            graphs.add(GraphType.LINE.name);
        }

        if (temporal == 1 && categorical > 0 && numerical == 0) {
            graphs.add(GraphType.BAR.name);
        }

        if (categorical == 1 && numerical == 1 && temporal == 0) {
            graphs.add(GraphType.PIE.name);
        }

        if ((categorical == 1 && numerical >= 1 && numerical <= 2 && temporal == 0) ||
                (categorical == 2 && numerical == 1 && temporal == 0) ||
                (numerical >= 2 && numerical <= 3 && temporal == 0 && categorical == 0)
        ) {
            graphs.add(GraphType.BUBBLE.name);
            graphs.add(GraphType.HEAT_MAP.name);
        }

        return graphs;
    }
}
