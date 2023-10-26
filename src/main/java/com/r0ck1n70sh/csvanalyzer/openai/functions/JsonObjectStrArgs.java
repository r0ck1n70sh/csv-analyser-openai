package com.r0ck1n70sh.csvanalyzer.openai.functions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class JsonObjectStrArgs {
    @JsonPropertyDescription("Any JSON Object string, for ex. { \"foo\": \"bar\"}")
    @JsonProperty(required = true)
    public String jsonStr;
}
