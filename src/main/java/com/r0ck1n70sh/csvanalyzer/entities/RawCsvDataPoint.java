package com.r0ck1n70sh.csvanalyzer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RAW_CSV_DATA_POINT")
@Getter
@Setter
public class RawCsvDataPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "meta_id")
    @JsonIgnore
    private RawCsvMeta meta;

    @ManyToOne
    @JoinColumn(name = "column_id")
    @JsonIgnore
    private RawCsvColumn column;

    private int index;

    private String data;

    @Override
    public String toString() {
        return String.format("""
                        {
                            "column": %s,
                            "index": %d,
                            "data": %s
                        },
                        """,
                column, index, data
        );
    }
}
