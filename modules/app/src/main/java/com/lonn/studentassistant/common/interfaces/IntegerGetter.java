package com.lonn.studentassistant.common.interfaces;

@FunctionalInterface
public interface IntegerGetter<T> {
    Integer getIntegerField(T entity);
}
