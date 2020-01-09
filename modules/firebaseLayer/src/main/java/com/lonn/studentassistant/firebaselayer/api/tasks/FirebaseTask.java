package com.lonn.studentassistant.firebaselayer.api.tasks;

import androidx.annotation.Nullable;

import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

public abstract class FirebaseTask<T, V> {
	protected FirebaseConnection firebaseConnection;

	public FirebaseTask(FirebaseConnection firebaseConnection) {
		this.firebaseConnection = firebaseConnection;
	}

	public abstract void onComplete(Consumer<T> onSuccess, @Nullable Consumer<V> onError);

	public final void onComplete(Consumer<T> onSuccess) {
		onComplete(onSuccess, null);
	}

	public final void onCompleteDoNothing() {
		onComplete(null, null);
	}
}
