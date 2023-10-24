package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.entities.ChatSession;

public interface ComponentFactory<E extends Component> {
    default void setCsvStr(String csvStr) {
        return;
    }

    default void setCsvName(String csvName) {
        return;
    }

    default void setUserPrompt(String userPrompt) {
        return;
    }

    E create(ChatSession chatSession);
}
