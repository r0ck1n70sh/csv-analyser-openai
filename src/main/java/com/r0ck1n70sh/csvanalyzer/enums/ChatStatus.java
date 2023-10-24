package com.r0ck1n70sh.csvanalyzer.enums;

import com.r0ck1n70sh.csvanalyzer.pipeline.*;

public enum ChatStatus {
    INITIALIZED(0, new InitializerFactory()),
    CSV_SAVED(1, new CsvSaverFactory()),
    CSV_PROCESSED(2, new CsvProcessorFactory()),
    ANALYTICS_COMPLETED(3, new AnalyticsFactory()),
    INVALID(-1, new DefaultFactory());

    private final int order;

    public final ComponentFactory factory;

    ChatStatus(int order, ComponentFactory factory) {
        this.order = order;
        this.factory = factory;
    }

    public ChatStatus getPrev() {
        return getByOrder(this.order - 1);
    }

    public ChatStatus getNext() {
        if (this == ANALYTICS_COMPLETED) return ANALYTICS_COMPLETED;
        return getByOrder(this.order + 1);
    }

    public static ChatStatus getByOrder(int order) {
        if (order < 0 || order >= ChatStatus.values().length) {
            return INVALID;
        }

        for (ChatStatus chatStatus : ChatStatus.values()) {
            if (chatStatus.order == order) {
                return chatStatus;
            }
        }

        return INVALID;
    }
}
