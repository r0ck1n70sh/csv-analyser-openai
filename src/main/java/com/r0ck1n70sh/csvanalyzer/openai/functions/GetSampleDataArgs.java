package com.r0ck1n70sh.csvanalyzer.openai.functions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class GetSampleDataArgs {

    @JsonPropertyDescription("Column Name, for example Year, Quantity")
    @JsonProperty(required = true)
    public String columnName;

    @JsonPropertyDescription("Sample output size, for example 1, 3, 10")
    @JsonProperty(required = true)
    public int outputSize;

    @Override
    public String toString() {
        return String.format("""
                        {
                            "columnName": %s,
                            "outputSize":  %d
                        }
                """, columnName, outputSize);
    }
}
