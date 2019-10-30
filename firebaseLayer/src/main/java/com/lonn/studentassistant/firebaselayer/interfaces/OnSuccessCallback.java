package com.lonn.studentassistant.firebaselayer.interfaces;

@FunctionalInterface
public interface OnSuccessCallback<T> {
    void callback(T arg);
}
