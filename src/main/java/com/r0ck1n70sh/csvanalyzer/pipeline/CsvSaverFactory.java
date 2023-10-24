package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.entities.ChatSession;
import lombok.Setter;

@Setter
public class CsvSaverFactory implements ComponentFactory<CsvSaverComponent> {
    private String csvName;
    private String csvStr;

    @Override
    public CsvSaverComponent create(ChatSession chatSession) {
        return new CsvSaverComponent(csvName, csvStr);
    }
}
