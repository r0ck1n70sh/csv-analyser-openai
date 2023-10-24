package com.r0ck1n70sh.csvanalyzer.enums;

import com.r0ck1n70sh.csvanalyzer.graphs.*;

public enum GraphType {
    BAR("bar", new BarGraphGenerator()),
    LINE("line", new LineGraphGenerator()),
    PIE("pie", new PieChartGenerator()),
    BUBBLE("bubble", new BubbleChartGenerator()),
    HEAT_MAP("heat_map", new HeatMapGenerator());

    public final String name;

    public final GraphGenerator generator;

    GraphType(String name, GraphGenerator generator) {
        this.name = name;
        this.generator = generator;
    }

    @Override
    public String toString() {
        return name;
    }
}
