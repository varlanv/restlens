package com.varlanv.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class Maybe<T> {

    private static final Maybe<?> EMPTY = new Maybe<>();

    @Nullable
    T value;

    private Maybe(T value) {
        this.value = value;
    }

    private Maybe() {
        this.value = null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Maybe<T> empty() {
        return (Maybe<T>) EMPTY;
    }

    public static <T> Maybe<T> of(T value) {
        return new Maybe<>(value);
    }

    public boolean isPresent() {
        return value != null;
    }

    public void ifPresent(Consumer<@NotNull T> consumer) {
        if (isPresent()) {
            consumer.accept(value);
        }
    }
}
