package com.varlanv.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OpenApiFieldType {

    INT32("int32"),
    INT64("int64"),
    STRING("string");

    String openApiName;
}
