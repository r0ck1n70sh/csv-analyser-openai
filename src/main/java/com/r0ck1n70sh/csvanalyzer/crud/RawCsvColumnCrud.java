package com.r0ck1n70sh.csvanalyzer.crud;

import com.r0ck1n70sh.csvanalyzer.entities.RawCsvColumn;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvDataPoint;
import com.r0ck1n70sh.csvanalyzer.repositories.RawCsvColumnRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Getter
@Setter
public class RawCsvColumnCrud {
    @Autowired
    private RawCsvColumnRepository repository;

    private static RawCsvColumnCrud instance;

    public static RawCsvColumnCrud getInstance() {
        if (instance == null) {
            instance = new RawCsvColumnCrud();
        }

        return instance;

    }

    public static void save(@NonNull RawCsvColumn entity) {
        List<RawCsvDataPoint> dataPoints = entity.getDataPoints();

        RawCsvDataPointCrud.saveAll(dataPoints);
        dataPoints.forEach(d -> d.setColumn(entity));

        RawCsvColumnCrud instance = getInstance();
        RawCsvColumnRepository repository = instance.getRepository();
        repository.save(entity);
    }
}
