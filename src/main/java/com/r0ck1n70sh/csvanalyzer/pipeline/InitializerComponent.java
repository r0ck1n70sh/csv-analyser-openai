package com.r0ck1n70sh.csvanalyzer.pipeline;

import com.r0ck1n70sh.csvanalyzer.enums.ChatStatus;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class InitializerComponent implements Component {
    @Override
    public ComponentResponse conduct() {
        ComponentResponse componentResponse = new ComponentResponse();
        componentResponse.setStatus(ChatStatus.INITIALIZED);

        return componentResponse;
    }
}
