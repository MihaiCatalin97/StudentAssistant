package com.lonn.studentassistant.firebaselayer.api.tasks;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.requests.Request;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

public class FirebaseListTask<T extends EntityViewModel<? extends BaseEntity>, V> extends FirebaseEntityTask<T, V> {
	private Request<T, V> request;

	public FirebaseListTask(FirebaseConnection firebaseConnection,
							Request<T, V> request) {
		super(firebaseConnection);
		this.request = request;
	}

	public void onComplete(Consumer<T> onSuccess, Consumer<V> onError) {
		firebaseConnection.execute(request.onSuccess(onSuccess)
				.onError(onError));
	}

	public void onComplete(Consumer<T> onSuccess){
		firebaseConnection.execute(request.onSuccess(onSuccess)
				.onError(onError));
	}
}
