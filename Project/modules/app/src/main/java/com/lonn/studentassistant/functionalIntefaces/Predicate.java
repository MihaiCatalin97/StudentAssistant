package com.lonn.studentassistant.functionalIntefaces;

@FunctionalInterface
public interface Predicate<T> {
	boolean test(T entity);
}
