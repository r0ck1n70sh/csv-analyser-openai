package com.r0ck1n70sh.csvanalyzer.enums;


public enum ColumnType {
    CATEGORICAL("categorical"),
    NUMERICAL("numerical"),
    TEMPORAL("temporal");

    private final String name;

    ColumnType(String name) {
        this.name = name;
    }

    public static ColumnType fromString(String name) {
        if (name == null) return null;

        for (ColumnType type : ColumnType.values()) {
            if (type.name.equals(name.toLowerCase())) {
                return type;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
