package com.lonn.studentassistant.common.interfaces;

@FunctionalInterface
public interface Predicate<T> {
    boolean test(T entity);
}
