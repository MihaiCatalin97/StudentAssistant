package com.lonn.studentassistant.firebaselayer.businessLayer.api.tasks;

import androidx.annotation.Nullable;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class FirebaseTask<T, V> {
	protected FirebaseConnection firebaseConnection;
	@Setter
	protected boolean subscribe = true;

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
