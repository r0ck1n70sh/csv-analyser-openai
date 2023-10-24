package com.r0ck1n70sh.csvanalyzer.pipeline;


import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsComponent implements Component {
    private final String userPrompt;

    private final List<ChatMessage> messages;

    public AnalyticsComponent(String userPrompt) {
        this.userPrompt = userPrompt;
        this.messages = new ArrayList<>();
    }

    @Override
    public ComponentResponse conduct() {
        return null;
    }
}
