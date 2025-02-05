package com.varlanv.impl;

import org.jetbrains.annotations.Contract;

import java.util.*;
import java.util.function.UnaryOperator;

class CollectionUtil {

    @Contract(pure = true)
    static <T> List<T> concatListsFinal(List<T> first, List<T> second) {
        var newList = new ArrayList<T>(first.size() + second.size());
        newList.addAll(first);
        newList.addAll(second);
        return Collections.unmodifiableList(newList);
    }

    @Contract(pure = true)
    static <T> List<T> concatToListFinal(List<T> first, T value) {
        var newList = new ArrayList<T>(first.size() + 1);
        newList.addAll(first);
        newList.add(value);
        return Collections.unmodifiableList(newList);
    }

    @Contract(pure = true)
    static <K, V, M extends Map<K, V>> M putToNewMap(K key, V value, M originalMap, UnaryOperator<M> newMapSupplier) {
        var newMap = newMapSupplier.apply(originalMap);
        newMap.put(
            Objects.requireNonNull(key, "Null keys are not allowed"),
            Objects.requireNonNull(value, "Null values are not allowed")
        );
        return newMap;
    }
}
