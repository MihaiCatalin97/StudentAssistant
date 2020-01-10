package com.lonn.studentassistant.functionalIntefaces;

@FunctionalInterface
public interface Function<T, U> {
	U apply(T argument);
}
