package com.r0ck1n70sh.csvanalyzer.pipeline;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DefaultComponent implements Component {

    public ComponentResponse conduct() {
        return new ComponentResponse();
    }
}
