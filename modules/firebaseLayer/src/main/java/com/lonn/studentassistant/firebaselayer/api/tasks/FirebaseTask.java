package com.lonn.studentassistant.firebaselayer.api.tasks;

import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public abstract class FirebaseTask<T, V> {
	protected Consumer<T> onSuccess;
	protected Consumer<V> onError;
	protected FirebaseConnection firebaseConnection;

	public FirebaseTask(FirebaseConnection firebaseConnection) {
		this.firebaseConnection = firebaseConnection;
	}

	public abstract void onComplete(Consumer<T> onSuccess, Consumer<V> onError);

	public abstract void onComplete(Consumer<T> onSuccess);
}
