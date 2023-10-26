package com.r0ck1n70sh.csvanalyzer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtils {
    public static boolean isValidJsonObjectString(String jsonStr) {
        if (jsonStr == null) return false;

        try {
            JsonElement jsonElem = parseJsonStr(jsonStr);
            return isValidJsonObjectElem(jsonElem);
        } catch (JsonParseException e) {
            return false;
        }
    }

    public static boolean isValidJsonObjectElem(JsonElement jsonElem) {
        if (jsonElem == null) return false;

        try {
            jsonElem.getAsJsonObject();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public static boolean isValidJsonArrayString(String jsonStr) {
        if (jsonStr == null) return false;

        try {
            JsonElement jsonElement = parseJsonStr(jsonStr);
            jsonElement.getAsJsonArray();
            return true;
        } catch (JsonParseException | IllegalStateException e) {
            return false;
        }
    }

    public static JsonObject parseJsonStrAsJsonObject(@NonNull String jsonStr) {
        JsonElement jsonElement = parseJsonStr(jsonStr);
        return jsonElement.getAsJsonObject();
    }

    public static JsonArray parseJsonStrAsJsonArray(@NonNull String jsonStr) {
        JsonElement jsonElement = parseJsonStr(jsonStr);
        return jsonElement.getAsJsonArray();
    }

    public static JsonElement parseJsonStr(@NonNull String jsonStr) throws JsonParseException {
        return new JsonParser().parse(jsonStr);
    }

    public static Map<String, String> jsonNodeAsMap(@NonNull JsonNode json) {
        Map<String, String> map = new HashMap<>();

        for (Iterator<String> it = json.fieldNames(); it.hasNext(); ) {
            String fieldName = it.next();
            map.put(fieldName, json.get(fieldName).asText());
        }

        return map;
    }

    public static JsonNode mapToJsonNode(@NonNull Map<String, String> map) throws JsonProcessingException {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);


        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonStr);
    }
}
