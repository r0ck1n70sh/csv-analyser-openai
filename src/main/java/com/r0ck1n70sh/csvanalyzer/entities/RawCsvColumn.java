package com.r0ck1n70sh.csvanalyzer.entities;

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

    @ManyToOne
    @JoinColumn(name = "meta")
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
