package com.lonn.studentassistant.validation.functionalInterfaces;

@FunctionalInterface
public interface Predicate<T> {
    boolean test(T entity);
}
