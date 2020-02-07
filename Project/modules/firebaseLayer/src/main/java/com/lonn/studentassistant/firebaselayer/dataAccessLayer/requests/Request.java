package com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests;

import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public abstract class Request<T, V> {
	protected Consumer<T> onSuccess;
	protected Consumer<V> onError;
}
