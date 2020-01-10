package com.lonn.studentassistant.functionalIntefaces;

@FunctionalInterface
public interface IntegerGetter<T> {
	Integer getIntegerField(T entity);
}
