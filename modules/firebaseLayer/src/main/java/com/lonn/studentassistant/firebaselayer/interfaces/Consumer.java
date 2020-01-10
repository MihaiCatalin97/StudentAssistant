package com.lonn.studentassistant.firebaselayer.interfaces;

@FunctionalInterface
public interface Consumer<T> {
	void consume(T arg);
}
