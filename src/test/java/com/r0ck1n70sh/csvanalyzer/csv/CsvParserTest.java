package com.r0ck1n70sh.csvanalyzer.csv;

import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertNotNull;

public class CsvParserTest {
    private  File csvFile;

    @Before
    public void readCsvFile() {
        String path = "src/test/resources/samplecsv/sales_data_sample.csv";
        csvFile = new File(path);
    }

    @Test
    public void verifyParse() throws IOException {
        InputStream inputStream = new FileInputStream(csvFile);
        byte[] bytes = inputStream.readAllBytes();
        String csvData = new String(bytes);

        CsvParser csvParser = new CsvParser(csvData, "sample_sales");
        RawCsvMeta rawCsvMeta = csvParser.parse();

        System.out.println(rawCsvMeta);
        assertNotNull(rawCsvMeta);
    }
}