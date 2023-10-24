package com.r0ck1n70sh.csvanalyzer.openai.functions;

import com.r0ck1n70sh.csvanalyzer.entities.RawCsvColumn;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvDataPoint;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import com.r0ck1n70sh.csvanalyzer.enums.ColumnType;
import com.r0ck1n70sh.csvanalyzer.utils.JsonUtils;
import com.theokanning.openai.completion.chat.ChatFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

    public static boolean isValidJsonStr(String jsonStr) {
        return JsonUtils.isValidJsonObjectString(jsonStr);
    }

    public static List<ColumnType> getColumnType() {
        return List.of(ColumnType.values());
    }

    public static List<String> getColumnNames(RawCsvMeta csv) {
        if (csv == null) return new ArrayList<>();

        List<RawCsvColumn> columns = csv.getColumns();
        if (columns == null) return new ArrayList<>();

        return columns.stream().map(RawCsvColumn::getName).toList();
    }
}
