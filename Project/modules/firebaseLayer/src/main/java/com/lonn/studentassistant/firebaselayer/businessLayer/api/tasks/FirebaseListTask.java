package com.lonn.studentassistant.firebaselayer.businessLayer.api.tasks;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.interfaces.Function;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.Request;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;

import java.util.List;

public class FirebaseListTask<T extends BaseEntity, V extends EntityViewModel<T>, U> extends FirebaseTask<List<V>, U> {
	private Request<List<T>, U> request;
	private Function<List<T>, List<V>> transformer;

	public FirebaseListTask(FirebaseConnection firebaseConnection,
							Request<List<T>, U> request) {
		super(firebaseConnection);
		this.request = request;
	}

	public void onComplete(Consumer<List<V>> onSuccess, Consumer<U> onError) {
		firebaseConnection.execute(createRequest(request, onSuccess, onError)
				.onError(onError));
	}

	private Request<List<T>, U> createRequest(Request<List<T>, U> request,
											  Consumer<List<V>> onSuccess,
											  Consumer<U> onError) {
		if (request instanceof GetRequest) {
			((GetRequest) request).subscribe(subscribe);
		}

		return request.onSuccess(entities -> {
			if (onSuccess != null) {
				onSuccess.consume(transformer.apply(entities));
			}
		})
				.onError(onError);
	}

	public FirebaseListTask<T, V, U> setTransformer(Function<List<T>, List<V>> transformer) {
		this.transformer = transformer;
		return this;
	}
}
