package com.varlanv;

import org.jetbrains.annotations.Contract;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class JsonMap {

    private final LinkedHashMap<String, Object> map;

    public JsonMap() {
        this(new LinkedHashMap<>());
    }

    JsonMap(LinkedHashMap<String, Object> map) {
        this.map = map;
    }

    @Contract(pure = true)
    public JsonMap put(String key, String value) {
        return putObj(key, value, str -> "\"" + str + "\"");
    }

    @Contract(pure = true)
    public JsonMap put(String key, Integer value) {
        return putObj(key, value, Function.identity());
    }

    @Contract(pure = true)
    public JsonMap putMany(String key, String... values) {
        return putManyObj(key, str -> "\"" + str + "\"", Arrays.asList(values));
    }

    @Contract(pure = true)
    public JsonMap putMany(String key, Integer... values) {
        return putManyObj(key, i -> i, Arrays.asList(values));
    }

    @Contract(pure = true)
    public JsonMap putMany(String key, BigDecimal... values) {
        return putManyObj(key, b -> b, Arrays.asList(values));
    }

    @Contract(pure = true)
    public JsonMap put(String key, BigDecimal value) {
        return putObj(key, value, b -> b);
    }

    @Contract(pure = true)
    private <T> JsonMap putObj(String key, T value, Function<T, Object> valueMapper) {
        var newMap = new LinkedHashMap<>(map);
        newMap.put("\"" + Objects.requireNonNull(key, "Null keys are not allowed") + "\"",
            valueMapper.apply(Objects.requireNonNull(value, "Null values are not allowed"))
        );
        return new JsonMap(newMap);
    }

    @Contract(pure = true)
    private <T> JsonMap putManyObj(String key, Function<T, Object> valueMapper, List<T> values) {
        if (values.isEmpty()) {
            return putObj(key, "[]", Function.identity());
        }
        var sb = new StringBuilder("[");
        var iterator = values.iterator();
        while (iterator.hasNext()) {
            sb.append(valueMapper.apply(Objects.requireNonNull(iterator.next())));
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        var newMap = new LinkedHashMap<>(map);
        newMap.put("\"" + Objects.requireNonNull(key, "Null keys are not allowed") + "\"",
            sb.toString()
        );
        return new JsonMap(newMap);
    }

    @Contract(pure = true)
    public String toJson() {
        if (map.isEmpty()) {
            return "{}";
        }
        var jsonBuilder = new StringBuilder("{\n");
        var iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            var entry = iter.next();
            jsonBuilder.append("  ").append(entry.getKey()).append(": ").append(entry.getValue());
            if (iter.hasNext()) {
                jsonBuilder.append(",\n");
            }
        }
        jsonBuilder.append("\n}");
        return jsonBuilder.toString();
    }

    @Override
    public String toString() {
        return toJson();
    }
}
