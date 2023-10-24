package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.entities.ChatSession;
import lombok.Setter;

@Setter
public class CsvProcessorFactory implements ComponentFactory<CsvProcessorComponent> {
    @Override
    public CsvProcessorComponent create(ChatSession chatSession) {
        return new CsvProcessorComponent(chatSession.getCsv());
    }
}
