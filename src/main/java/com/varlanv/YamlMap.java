package com.varlanv;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class YamlMap {

    LinkedHashMap<String, Object> map;

    public YamlMap() {
        this(new LinkedHashMap<>());
    }

    YamlMap(LinkedHashMap<String, Object> map) {
        this.map = map;
    }

    @Contract(pure = true)
    public YamlMap put(String key, String value) {
        return putObj(key, value, Function.identity());
    }

    @Contract(pure = true)
    public YamlMap put(String key, Integer value) {
        return putObj(key, value, Function.identity());
    }

    @Contract(pure = true)
    public YamlMap putMany(String key, String... values) {
        return putManyObj(key, s -> s, Arrays.asList(values));
    }

    @Contract(pure = true)
    public YamlMap putMany(String key, Integer... values) {
        return putManyObj(key, i -> i, Arrays.asList(values));
    }

    @Contract(pure = true)
    public YamlMap putMany(String key, BigDecimal... values) {
        return putManyObj(key, b -> b, Arrays.asList(values));
    }

    @Contract(pure = true)
    public YamlMap put(String key, BigDecimal value) {
        return putObj(key, value, b -> b);
    }

    @Contract(pure = true)
    private <T> YamlMap putObj(String key, T value, Function<T, Object> valueMapper) {
        var newMap = new LinkedHashMap<>(map);
        newMap.put(Objects.requireNonNull(key, "Null keys are not allowed"),
            " " + valueMapper.apply(Objects.requireNonNull(value, "Null values are not allowed"))
        );
        return new YamlMap(newMap);
    }

    @Contract(pure = true)
    private <T> YamlMap putManyObj(String key, Function<T, Object> valueMapper, List<T> values) {
        if (values.isEmpty()) {
            return putObj(key, "", Function.identity());
        }
        var sb = new StringBuilder("\n");
        var iterator = values.iterator();
        while (iterator.hasNext()) {
            sb.append("  - ").append(valueMapper.apply(Objects.requireNonNull(iterator.next())));
            if (iterator.hasNext()) {
                sb.append("\n");
            }
        }
        var newMap = new LinkedHashMap<>(map);
        newMap.put(Objects.requireNonNull(key, "Null keys are not allowed"),
            sb.toString()
        );
        return new YamlMap(newMap);
    }

    @Contract(pure = true)
    public String toYaml() {
        if (map.isEmpty()) {
            return "";
        }
        var yamlBuilder = new StringBuilder();
        var iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            var entry = iter.next();
            yamlBuilder.append(entry.getKey()).append(":").append(entry.getValue());
            if (iter.hasNext()) {
                yamlBuilder.append("\n");
            }
        }
        yamlBuilder.append("\n");
        return yamlBuilder.toString();
    }

    @Override
    public String toString() {
        return toYaml();
    }
}
