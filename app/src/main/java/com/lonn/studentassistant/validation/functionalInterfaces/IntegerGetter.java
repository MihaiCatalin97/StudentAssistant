package com.lonn.studentassistant.validation.functionalInterfaces;

@FunctionalInterface
public interface IntegerGetter<T> {
    Integer getIntegerField(T entity);
}
