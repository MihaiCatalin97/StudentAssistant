package com.lonn.studentassistant.functionalIntefaces;

@FunctionalInterface
public interface BiFunction<T, U, V> {
	V apply(T arg1, U arg2);
}
