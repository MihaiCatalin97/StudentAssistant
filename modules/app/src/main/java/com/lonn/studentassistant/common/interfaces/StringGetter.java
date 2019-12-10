package com.lonn.studentassistant.common.interfaces;

@FunctionalInterface
public interface StringGetter<T> {
    String getStringField(T entity);
}
