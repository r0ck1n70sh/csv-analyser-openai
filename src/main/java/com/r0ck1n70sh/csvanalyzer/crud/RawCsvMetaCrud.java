package com.r0ck1n70sh.csvanalyzer.crud;

import com.r0ck1n70sh.csvanalyzer.entities.RawCsvColumn;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvDataPoint;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import com.r0ck1n70sh.csvanalyzer.repositories.RawCsvMetaRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class RawCsvMetaCrud {
    RawCsvMetaRepository repository;

    private static RawCsvMetaCrud instance;

    public static RawCsvMetaCrud getInstance() {
        if (instance == null) {
            instance = new RawCsvMetaCrud();
        }

        return instance;
    }

    public static void save(@NonNull RawCsvMeta entity) {
        List<RawCsvColumn> columns = entity.getColumns();
        columns.forEach(RawCsvColumnCrud::save);
        columns.forEach(c -> c.setMeta(entity));

        List<RawCsvDataPoint> dataPoints = entity.getDataPoints();
        dataPoints.forEach(d -> d.setMeta(entity));

        RawCsvMetaCrud instance = getInstance();
        RawCsvMetaRepository repository = instance.getRepository();

        repository.save(entity);
    }
}
