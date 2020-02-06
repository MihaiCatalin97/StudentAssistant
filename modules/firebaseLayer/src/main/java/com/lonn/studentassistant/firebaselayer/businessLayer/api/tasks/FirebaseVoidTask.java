package com.lonn.studentassistant.firebaselayer.businessLayer.api.tasks;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.Request;

public class FirebaseVoidTask<U extends Throwable> extends FirebaseTask<Void, U> {
	protected Request<Void, U> request;

	public FirebaseVoidTask(FirebaseConnection firebaseConnection,
							Request<Void, U> request) {
		super(firebaseConnection);
		this.request = request;
	}

	public void onComplete(Consumer<Void> onSuccess, Consumer<U> onError) {
		firebaseConnection.execute(createRequest(request, onSuccess, onError)
				.onError(onError));
	}

	protected Request<Void, U> createRequest(Request<Void, U> request,
											 Consumer<Void> onSuccess,
											 Consumer<U> onError) {
		return request.onSuccess(none -> {
			if (onSuccess != null) {
				onSuccess.consume(null);
			}
		}).onError(onError);
	}
}