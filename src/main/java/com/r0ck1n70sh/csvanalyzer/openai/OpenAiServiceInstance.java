package com.r0ck1n70sh.csvanalyzer.openai;

import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;


public class OpenAiServiceInstance {
    public static final String OPEN_AI_API_TOKEN = "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";
    public static final String OPEN_AI_MODEL = "gpt-3.5-turbo";

    private static OpenAiService instance;

    public static OpenAiService getInstance() {
        if (instance == null) {
            instance = new OpenAiService(OPEN_AI_API_TOKEN, Duration.ofMinutes(60));
        }

        return instance;
    }
}
