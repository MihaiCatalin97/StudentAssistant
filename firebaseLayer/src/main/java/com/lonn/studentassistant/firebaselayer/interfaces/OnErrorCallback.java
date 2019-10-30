package com.lonn.studentassistant.firebaselayer.interfaces;

@FunctionalInterface
public interface OnErrorCallback<T> {
    void callback(T arg);
}
