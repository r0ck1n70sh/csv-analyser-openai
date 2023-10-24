package com.r0ck1n70sh.csvanalyzer.crud;

import com.r0ck1n70sh.csvanalyzer.entities.RawCsvDataPoint;
import com.r0ck1n70sh.csvanalyzer.repositories.RawCsvDataPointRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@Setter
public class RawCsvDataPointCrud {
    private RawCsvDataPointRepository repository;

    private static RawCsvDataPointCrud instance;

    public static RawCsvDataPointCrud getInstance() {
        if (instance == null) {
            instance = new RawCsvDataPointCrud();
        }

        return instance;
    }

    public static void save(@NonNull RawCsvDataPoint entity) {
        RawCsvDataPointCrud instance = getInstance();
        RawCsvDataPointRepository repository = instance.repository;
        repository.save(entity);
    }

    public static void saveAll(@NonNull List<RawCsvDataPoint> entities) {
        RawCsvDataPointCrud instance = getInstance();
        RawCsvDataPointRepository repository = instance.repository;
        repository.saveAll(entities);
    }
}
