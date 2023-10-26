package com.r0ck1n70sh.csvanalyzer.openai.functions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class JsonArrayStrArgs {
    @JsonPropertyDescription("Any JSON Array string, for ex. [ \"foo\", \"bar\", { \"foo\": \"bar\" }]")
    @JsonProperty(required = true)
    public String jsonStr;
}
