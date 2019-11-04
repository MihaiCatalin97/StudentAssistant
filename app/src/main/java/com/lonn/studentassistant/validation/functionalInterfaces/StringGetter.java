package com.lonn.studentassistant.validation.functionalInterfaces;

@FunctionalInterface
public interface StringGetter<T> {
    String getStringField(T entity);
}
