package com.r0ck1n70sh.csvanalyzer.repositories;

import com.r0ck1n70sh.csvanalyzer.entities.RawCsvDataPoint;
import org.springframework.data.repository.CrudRepository;


public interface RawCsvDataPointRepository extends CrudRepository<RawCsvDataPoint, Long> {
};
