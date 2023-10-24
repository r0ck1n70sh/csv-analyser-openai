package com.r0ck1n70sh.csvanalyzer.utils;

import com.google.gson.*;
import lombok.NonNull;

public class JsonUtils {
    public static boolean isValidJsonObjectString(String jsonStr) {
        if (jsonStr == null) return false;

        try {
            JsonElement jsonElement = parseJsonStr(jsonStr);
            jsonElement.getAsJsonObject();
            return true;
        } catch (JsonParseException | IllegalStateException e) {
            return false;
        }
    }

    public static JsonObject parseJsonStrAsJsonObject(@NonNull String jsonStr) {
        JsonElement jsonElement = parseJsonStr(jsonStr);
        return jsonElement.getAsJsonObject();
    }

    public static JsonElement parseJsonStr(@NonNull String jsonStr) throws JsonParseException {
        return new JsonParser().parse(jsonStr);
    }
}
