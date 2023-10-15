package com.r0ck1n70sh.csvanalyzer.csv;

import com.opencsv.CSVReader;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvColumn;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvDataPoint;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    private final String csvData;

    private final String csvName;

    public CsvParser(String csvData, String csvName) {
        this.csvData = csvData;
        this.csvName = csvName;
    }

    public RawCsvMeta parse() {
        List<String[]> rowsList = readAllLines();
        RawCsvMeta meta = new RawCsvMeta();
        meta.setName(csvName);

        if (rowsList.isEmpty()) {
            return meta;
        }

        String[] columnStr = rowsList.get(0);
        List<RawCsvColumn> columns = createRawCsvColumns(columnStr);
//        columns.forEach(column -> column.setMeta(meta));
        meta.setColumns(columns);

        List<String[]> dataPointsStr = rowsList.subList(1, rowsList.size());
        List<RawCsvDataPoint> dataPoints = createRawCsvDataPoints(dataPointsStr, columns);
//        dataPoints.forEach(dataPoint -> dataPoint.setMeta(meta));
        meta.setDataPoints(dataPoints);

        meta.setSize(dataPoints.size());

        return meta;
    }

    private List<String[]> readAllLines() {
        try {
            StringReader stringReader = new StringReader(csvData);
            CSVReader csvReader = new CSVReader(stringReader);
            return csvReader.readAll();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private List<RawCsvColumn> createRawCsvColumns(String[] columnsStr) {
        List<RawCsvColumn> columns = new ArrayList<>();

        for (String columnStr : columnsStr) {
            RawCsvColumn rawCsvColumn = new RawCsvColumn();
            rawCsvColumn.setName(columnStr);
            columns.add(rawCsvColumn);
        }

        return columns;
    }

    private List<RawCsvDataPoint> createRawCsvDataPoints(List<String[]> dataPointsStr, List<RawCsvColumn> columns) {
        List<RawCsvDataPoint> dataPoints = new ArrayList<>();

        for (int index = 0; index < dataPointsStr.size(); index++) {
            String[] dataPointRow = dataPointsStr.get(index);

            for (int i = 0; i < dataPointRow.length; i++) {
                String dataPointStr = dataPointRow[i];
                RawCsvColumn column = columns.get(i);

                RawCsvDataPoint dataPoint = new RawCsvDataPoint();
                dataPoint.setData(dataPointStr);
                dataPoint.setIndex(index);
//                dataPoint.setColumn(column);

                column.getDataPoints().add(dataPoint);
                dataPoints.add(dataPoint);
            }
        }

        return dataPoints;
    }
}
