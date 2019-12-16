package com.lonn.studentassistant.common.interfaces;

import java.util.List;

@FunctionalInterface
public interface Supplier<T> {
    List<T> generate();
}
