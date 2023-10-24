package com.r0ck1n70sh.csvanalyzer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RAW_CSV_META")
@Getter
@Setter
public class RawCsvMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private int size;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meta")
    @ElementCollection
    private List<RawCsvColumn> columns;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meta")
    @ElementCollection
    @JsonIgnore
    private List<RawCsvDataPoint> dataPoints;

    @OneToOne
    @JsonIgnore
    private ChatSession chatSession;

    public RawCsvMeta() {
        this.columns = new ArrayList<>();
        this.dataPoints = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("""
                        {
                            "id": %d,
                            "name": %s,
                            "size": %d,
                            "column": %s,
                            "dataPoints": %s
                        }
                        """,
                id, name, size, columns, dataPoints);
    }
}
