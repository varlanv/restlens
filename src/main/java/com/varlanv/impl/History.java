package com.varlanv.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class History {

    ConcurrentMap<String, Specifications.OpenApiField<?>> fieldsHistory = new ConcurrentHashMap<>();

    public void record(Specifications.RequestBody.Steps.ResultStep resultStep) {
        for (var field : resultStep.fields.fields) {
            fieldsHistory.put(field.path, field);
        }
    }

    public String toYaml() {
        return null;
    }
}
