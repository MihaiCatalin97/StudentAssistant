package com.lonn.studentassistant.functionalIntefaces;

@FunctionalInterface
public interface StringGetter<T> {
	String getStringField(T entity);
}
