package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.entities.ChatSession;
import lombok.Setter;

@Setter
public class AnalyticsFactory implements ComponentFactory<AnalyticsComponent> {
    private String userPrompt;

    @Override
    public AnalyticsComponent create(ChatSession chatSession) {
        return new AnalyticsComponent(userPrompt, chatSession.getMessages(), chatSession.getCsv());
    }
}
