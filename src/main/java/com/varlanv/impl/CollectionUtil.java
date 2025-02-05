package com.varlanv.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CollectionUtil {

    static <T> List<T> concatListsFinal(List<T> first, List<T> second) {
        var newList = new ArrayList<T>(first.size() + second.size());
        newList.addAll(first);
        newList.addAll(second);
        return Collections.unmodifiableList(newList);
    }

    static <T> List<T> concatToListFinal(List<T> first, T value) {
        var newList = new ArrayList<T>(first.size() + 1);
        newList.addAll(first);
        newList.add(value);
        return Collections.unmodifiableList(newList);
    }
}
