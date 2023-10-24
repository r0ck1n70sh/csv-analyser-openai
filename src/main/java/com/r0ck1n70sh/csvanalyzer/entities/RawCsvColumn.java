package com.r0ck1n70sh.csvanalyzer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.r0ck1n70sh.csvanalyzer.enums.ColumnType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RAW_CSV_COLUMN")
@Getter
@Setter
public class RawCsvColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @Enumerated
    private ColumnType type;

    private String unit;

    @ManyToOne
    @JoinColumn(name = "meta_id")
    @JsonIgnore
    private RawCsvMeta meta;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "column")
    private List<RawCsvDataPoint> dataPoints;

    public RawCsvColumn() {
        this.dataPoints = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name;
    }
}
