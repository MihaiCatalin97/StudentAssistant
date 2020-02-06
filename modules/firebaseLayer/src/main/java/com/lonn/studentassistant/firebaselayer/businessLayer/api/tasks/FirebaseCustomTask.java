package com.lonn.studentassistant.firebaselayer.businessLayer.api.tasks;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.Request;

import lombok.experimental.Accessors;

@Accessors(chain = true)
public class FirebaseCustomTask<INT, EXT, ERR extends Exception> extends FirebaseTask<INT, ERR> {
	protected Request<INT, ERR> request;

	public FirebaseCustomTask(FirebaseConnection firebaseConnection,
							  Request<INT, ERR> request) {
		super(firebaseConnection);
		this.request = request;
	}

	public FirebaseCustomTask<INT, EXT, ERR> subscribe(boolean subscribe) {
		this.subscribe = subscribe;
		return this;
	}

	public void onComplete(Consumer<INT> onSuccess, Consumer<ERR> onError) {
		firebaseConnection.execute(createRequest(request, onSuccess, onError)
				.onError(onError));
	}

	protected Request<INT, ERR> createRequest(Request<INT, ERR> request,
											  Consumer<INT> onSuccess,
											  Consumer<ERR> onError) {
		return request.onSuccess(result -> {
			if (onSuccess != null) {
				onSuccess.consume(null);
			}
		}).onError(onError);
	}
}
