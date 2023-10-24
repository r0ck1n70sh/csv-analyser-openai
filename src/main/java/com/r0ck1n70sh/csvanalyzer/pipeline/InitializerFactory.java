package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.entities.ChatSession;

public class InitializerFactory implements ComponentFactory<InitializerComponent> {
    @Override
    public InitializerComponent create(ChatSession chatSession) {
        return null;
    }
}
