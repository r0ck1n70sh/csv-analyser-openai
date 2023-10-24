package com.r0ck1n70sh.csvanalyzer.pipeline;

import lombok.Getter;
import lombok.Setter;

import java.util.Base64;


@Getter
@Setter
public class PipelineRequest {
    private String csvName;
    private String csvStrBase64;
    private String userPrompt;

    public String getCsvStr() {
        if (csvStrBase64 == null) return null;

        byte[] bytes = Base64.getDecoder().decode(csvStrBase64);
        return new String(bytes);
    }

    @Override
    public String toString() {
        return String.format(
                """
                        {
                            "csvName": %s,
                            "csvStr": %s,
                            "userPrompt": %s
                        }
                        """,
                csvName, getCsvStr(), userPrompt
        );
    }
}
