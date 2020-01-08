package com.lonn.studentassistant.firebaselayer.api.tasks;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.requests.Request;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.List;

public class FirebaseSingleTask<T extends EntityViewModel<? extends BaseEntity>, V> extends FirebaseTask<T, V> {
	private Request<List<T>, V> request;

	public FirebaseSingleTask(FirebaseConnection firebaseConnection,
							  Request<List<T>, V> request) {
		super(firebaseConnection);
		this.request = request;
	}

	public void onComplete(Consumer<T> onSuccess, Consumer<V> onError) {
		firebaseConnection.execute(request.onSuccess(entities -> {
			if (entities.size() > 0) {
				onSuccess.consume(entities.get(0));
			}
		})
				.onError(onError));
	}

	public void onComplete(Consumer<T> onSuccess) {
		firebaseConnection.execute(request.onSuccess(entities -> {
			if (entities.size() > 0) {
				onSuccess.consume(entities.get(0));
			}
		})
				.onError(onError));
	}
}
