package com.r0ck1n70sh.csvanalyzer.enums;

import com.r0ck1n70sh.csvanalyzer.graphs.*;

public enum GraphType {
    BAR("bar_chart"),
    LINE("line_chart"),
    PIE("pie_chart"),
    BUBBLE("bubble_map"),
    HEAT_MAP("heat_map");

    public final String name;

    GraphType(String name) {
        this.name = name;
    }

    public static GraphType fromString(String name) {
        for (GraphType graphType : GraphType.values()) {
            if (graphType.name.equals(name)) {
                return graphType;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
