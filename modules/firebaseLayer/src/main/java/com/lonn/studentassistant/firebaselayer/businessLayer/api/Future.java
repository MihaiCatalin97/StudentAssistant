package com.lonn.studentassistant.firebaselayer.businessLayer.api;


import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class Future<T, E> {
    @Getter
    @Setter
    private Consumer<T> onSuccess;
    @Getter
    @Setter
    private Consumer<E> onError;

    public void complete(T result) {
        if (onSuccess != null) {
            onSuccess.consume(result);
        }
    }

    public void completeExceptionally(E exception) {
        if (onError != null) {
            onError.consume(exception);
        }
    }
}
