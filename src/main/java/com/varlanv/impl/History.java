package com.varlanv.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class History {

    ConcurrentMap<String, IndexedValue<Specifications.OpenApiField<?>>> fieldsHistory = new ConcurrentHashMap<>();

    public void record(List<Specifications.OpenApiField<?>> fields) {
        for (int i = 0, fieldsSize = fields.size(); i < fieldsSize; i++) {
            var field = fields.get(i);
            fieldsHistory.put(field.path, new IndexedValue<>(i, field));
        }
    }

    public String toYaml() {
        var yamlMap = new YamlMap();
        var fields = new ArrayList<>(fieldsHistory.values());
        fields.sort(Comparator.comparingInt(IndexedValue::index));
        for (var indexedField : fields) {
            var field = indexedField.value();
            var map = new LinkedHashMap<String, Object>();
            map.put("example", field.exampleValue);
            map.put("type", field.openApiType.openApiName());
            map.put("required", field.required);
            field.defaultValue.ifPresent(f -> map.put("default", f));
            map.putAll(field.options);
            yamlMap = yamlMap.putNested(field.path, map);
        }
        return yamlMap.toYaml();
    }
}
