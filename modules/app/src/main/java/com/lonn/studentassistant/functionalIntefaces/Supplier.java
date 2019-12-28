package com.lonn.studentassistant.functionalIntefaces;

import java.util.List;

@FunctionalInterface
public interface Supplier<T> {
    List<T> generate();
}
