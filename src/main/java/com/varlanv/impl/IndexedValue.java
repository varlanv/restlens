package com.varlanv.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IndexedValue<T> {

    Integer index;
    T value;
}
