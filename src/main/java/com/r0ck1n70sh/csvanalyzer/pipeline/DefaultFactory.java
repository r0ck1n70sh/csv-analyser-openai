package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.entities.ChatSession;

public class DefaultFactory implements ComponentFactory<DefaultComponent> {
    @Override
    public DefaultComponent create(ChatSession chatSession) {
        return new DefaultComponent();
    }
}
