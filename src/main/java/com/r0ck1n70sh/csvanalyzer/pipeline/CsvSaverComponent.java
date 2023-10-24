package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.csv.CsvParser;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import com.r0ck1n70sh.csvanalyzer.enums.ChatStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CsvSaverComponent implements Component {
    private final String csvName;
    private final String csvStr;

    @Override
    public ComponentResponse conduct() {
        ComponentResponse response = new ComponentResponse();

        if (csvName == null || csvStr == null) {
            response.setStatus(ChatStatus.INVALID);
            return response;
        }

        CsvParser csvParser = new CsvParser(csvStr, csvName);
        RawCsvMeta csv = csvParser.parse();

        response.setCsv(csv);
        response.setStatus(ChatStatus.CSV_SAVED);

        return response;
    }
}
