package com.r0ck1n70sh.csvanalyzer.repositories;

import com.r0ck1n70sh.csvanalyzer.entities.RawCsvColumn;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import org.springframework.data.repository.CrudRepository;

public interface RawCsvColumnRepository extends CrudRepository<RawCsvColumn, Long> {
};
