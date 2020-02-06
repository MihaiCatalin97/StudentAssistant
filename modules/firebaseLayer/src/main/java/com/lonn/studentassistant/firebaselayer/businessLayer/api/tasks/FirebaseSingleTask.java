package com.lonn.studentassistant.firebaselayer.businessLayer.api.tasks;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.interfaces.Function;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.Request;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;

import java.util.List;

public class FirebaseSingleTask<T extends BaseEntity, V extends EntityViewModel<T>, U> extends FirebaseTask<V, U> {
	private Request<List<T>, U> request;
	private Function<T, ? extends V> transformer;

	public FirebaseSingleTask(FirebaseConnection firebaseConnection,
							  Request<List<T>, U> request) {
		super(firebaseConnection);
		this.request = request;
	}

	public void onComplete(Consumer<V> onSuccess, Consumer<U> onError) {
		firebaseConnection.execute(createRequest(request, onSuccess, onError)
				.onError(onError));
	}

	private Request<List<T>, U> createRequest(Request<List<T>, U> request,
											  Consumer<V> onSuccess,
											  Consumer<U> onError) {
		if (request instanceof GetRequest) {
			((GetRequest) request).subscribe(subscribe);
		}

		return request.onSuccess(entities -> {
			if (entities.size() > 0 && onSuccess != null) {
				onSuccess.consume(transformer.apply(entities.get(0)));
			}
		}).onError(onError);
	}

	public FirebaseSingleTask<T, V, U> setTransformer(Function<T, ? extends V> transformer) {
		this.transformer = transformer;
		return this;
	}
}
