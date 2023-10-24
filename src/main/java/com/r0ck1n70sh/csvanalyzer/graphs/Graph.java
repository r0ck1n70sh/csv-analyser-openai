package com.r0ck1n70sh.csvanalyzer.graphs;

import com.r0ck1n70sh.csvanalyzer.enums.ColumnType;
import com.r0ck1n70sh.csvanalyzer.enums.GraphType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Graph {
    private GraphType type;

    private List<String> x;
    private ColumnType x_type;

    private List<String> y;
    private ColumnType y_type;

    private List<String> z;
    private ColumnType z_type;

    @Override
    public String toString() {
        return String.format(
                """
                        {
                            "type": %s,
                            "x": %s,
                            "x_type": %s,
                            "y": %s,
                            "y_type": %s,
                            "z": %s.
                            "z_type": %s.
                        }
                        """,
                type, x, x_type, y, y_type, z, z_type
        );
    }
}
