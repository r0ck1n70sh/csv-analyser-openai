package com.r0ck1n70sh.csvanalyzer.openai.functions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class ColumnNameArgs {

    @JsonPropertyDescription("Column Name, for example Year, Quantity")
    @JsonProperty(required = true)
    public String columnName;


    @Override
    public String toString() {
        return String.format("""
                        {
                            "columnName": %s
                        }
                """, columnName);
    }
}
