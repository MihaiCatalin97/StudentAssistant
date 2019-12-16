package com.lonn.studentassistant.common.interfaces;

@FunctionalInterface
public interface Function<T, U> {
    U apply(T argument);
}
