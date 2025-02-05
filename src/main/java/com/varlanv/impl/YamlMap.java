package com.varlanv.impl;

import lombok.experimental.PackagePrivate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlMap {

    @PackagePrivate
    @VisibleForTesting
    LinkedHashMap<String, Object> state;

    public YamlMap() {
        this(new LinkedHashMap<>());
    }

    YamlMap(LinkedHashMap<String, Object> state) {
        this.state = state;
    }

    @Contract(pure = true)
    public YamlMap putNested(String key, Map<String, ?> map) {
        return new YamlMap(
            CollectionUtil.putToNewMap(
                key,
                map,
                state,
                LinkedHashMap::new
            )
        );
    }

    @Contract(pure = true)
    public YamlMap put(String key, String value) {
        return putObj(key, value);
    }

    @Contract(pure = true)
    public YamlMap put(String key, Integer value) {
        return putObj(key, value);
    }

    @Contract(pure = true)
    public YamlMap putMany(String key, String... values) {
        return putManyObj(key, Arrays.asList(values));
    }

    @Contract(pure = true)
    public YamlMap putMany(String key, Integer... values) {
        return putManyObj(key, Arrays.asList(values));
    }

    @Contract(pure = true)
    public YamlMap putMany(String key, Double... values) {
        return putManyObj(key, Arrays.asList(values));
    }

    @Contract(pure = true)
    public YamlMap put(String key, Double value) {
        return putObj(key, value);
    }

    @Contract(pure = true)
    private <T> YamlMap putObj(String key, T value) {
        return new YamlMap(
            CollectionUtil.putToNewMap(
                key,
                value,
                state,
                LinkedHashMap::new
            )
        );
    }

    @Contract(pure = true)
    private <T> YamlMap putManyObj(String key, List<T> values) {
        if (values.isEmpty()) {
            return putObj(key, "");
        }
        return new YamlMap(
            CollectionUtil.putToNewMap(
                key,
                values,
                state,
                LinkedHashMap::new
            )
        );
    }

    @Contract(pure = true)
    public String toYaml() {
        return toYamlInner(this.state, 0);
    }

    @Contract(pure = true)
    private String toYamlInner(Map<String, Object> nestedMap, Integer nestLevel) {
        if (nestedMap.isEmpty()) {
            return "";
        }
        var iter = nestedMap.entrySet().iterator();
        var sb = new StringBuilder();
        while (iter.hasNext()) {
            var entry = iter.next();
            var key = entry.getKey();
            var val = entry.getValue();
            if (val instanceof Map) {
                @SuppressWarnings("unchecked")
                var mapVal = (Map<String, Object>) val;
                var innerYaml = toYamlInner(mapVal, nestLevel + 2);
                sb.append(" ".repeat(nestLevel)).append(key).append(":\n").append(innerYaml);
            } else if (val instanceof List) {
                var listSb = new StringBuilder(" ".repeat(nestLevel)).append(key).append(":\n");
                for (var listVal : (List<?>) val) {
                    if (listVal instanceof Map || listVal instanceof List) {
                        throw new RuntimeException("Nested lists or maps are not supported");
                    } else {
                        listSb.append(" ".repeat(nestLevel + 2)).append("- ").append(listVal.toString()).append("\n");
                    }
                }
                sb.append(listSb);
            } else {
                if (nestLevel > 1) {
                    sb.append(" ".repeat(nestLevel));
                }
                sb.append(entry.getKey()).append(": ").append(val).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toYaml();
    }
}
