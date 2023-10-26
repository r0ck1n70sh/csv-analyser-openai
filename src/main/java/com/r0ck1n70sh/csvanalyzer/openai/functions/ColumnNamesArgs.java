package com.r0ck1n70sh.csvanalyzer.openai.functions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public class ColumnNamesArgs {
    @JsonPropertyDescription("List of Column Name ex. [ \"Items\", \"Year\", \"Sales\" ] ")
    @JsonProperty(required = true)
    public List<String> columnNames;
}
