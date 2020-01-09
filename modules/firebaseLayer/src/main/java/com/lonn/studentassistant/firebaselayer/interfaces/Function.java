package com.lonn.studentassistant.firebaselayer.interfaces;

@FunctionalInterface
public interface Function<T, U> {
    U apply(T argument);
}
