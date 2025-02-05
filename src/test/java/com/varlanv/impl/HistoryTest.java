package com.varlanv.impl;

import com.varlanv.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class HistoryTest extends BaseTest {

    @Test
    void asd() {
        var history = new History();
        history.record(List.of(
                new Specifications.OpenApiField<>(
                    "int1",
                    true,
                    OpenApiFieldType.INT32,
                    3,
                    Maybe.empty(),
                    Map.of("minimum", 4)
                ),
                new Specifications.OpenApiField<>(
                    "int2",
                    false,
                    OpenApiFieldType.INT32,
                    5,
                    Maybe.empty(),
                    Map.of("minimum", 8)
                )
            )
        );
        var yaml = history.toYaml();
        System.out.println(yaml);
    }
}
