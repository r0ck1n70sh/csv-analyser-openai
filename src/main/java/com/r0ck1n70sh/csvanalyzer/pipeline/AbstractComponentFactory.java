package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.enums.ChatStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AbstractComponentFactory {
    private final String csvStr;
    private final String csvName;
    private final String userPrompt;
    private final ChatStatus status;

    public ComponentFactory create() {
        if (status == null) {
            return null;
        }

        ComponentFactory componentFactory = status.factory;
        componentFactory.setCsvStr(csvStr);
        componentFactory.setCsvName(csvName);

        componentFactory.setUserPrompt(userPrompt);

        return componentFactory;
    }
}
