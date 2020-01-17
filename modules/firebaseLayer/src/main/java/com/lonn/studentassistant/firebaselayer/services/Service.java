package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseListTask;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseSingleTask;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseTask;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseVoidTask;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.DeleteAllRequest;
import com.lonn.studentassistant.firebaselayer.requests.DeleteByIdRequest;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.List;

import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public abstract class Service<T extends BaseEntity, V extends Throwable, U extends EntityViewModel<T>> {
	protected FirebaseConnection firebaseConnection;
	protected FirebaseApi firebaseApi;
	protected ViewModelAdapter<T, U> adapter;

	protected Service(FirebaseConnection firebaseConnection) {
		this.firebaseConnection = firebaseConnection;
		this.firebaseApi = FirebaseApi.getApi(null);
	}

	public FirebaseTask<List<U>, V> getAll() {
		return new FirebaseListTask<T, U, V>(firebaseConnection,
				new GetRequest<T, V>().databaseTable(getDatabaseTable()))
				.setTransformer(this::transform);
	}

	public FirebaseTask<U, V> getById(String id) {
		return new FirebaseSingleTask<T, U, V>(firebaseConnection,
				new GetRequest<T, V>()
						.databaseTable(getDatabaseTable())
						.predicate(where(ID).equalTo(id)))
				.setTransformer(this::transform);
	}

	public FirebaseTask<Void, V> save(U entityViewModel) {
		return new FirebaseVoidTask<>(firebaseConnection,
				new SaveRequest<T, V>()
						.databaseTable(getDatabaseTable())
						.entity(adapter.adapt(entityViewModel)));
	}

	public FirebaseTask<Void, V> save(T entity) {
		return new FirebaseVoidTask<>(firebaseConnection,
				new SaveRequest<T, V>()
						.databaseTable(getDatabaseTable())
						.entity(entity));
	}

	public FirebaseTask<Void, Exception> deleteAll() {
		return new FirebaseVoidTask<>(firebaseConnection,
				new DeleteAllRequest()
						.databaseTable(getDatabaseTable()));
	}

	public FirebaseTask<Void, Exception> deleteById(String id) {
		return new FirebaseVoidTask<>(firebaseConnection,
				new DeleteByIdRequest()
						.databaseTable(getDatabaseTable())
						.key(id));
	}

	protected U transform(T entity) {
		return adapter.adapt(entity);
	}

	protected List<U> transform(List<T> entities) {
		return adapter.adapt(entities);
	}

	protected abstract DatabaseTable<T> getDatabaseTable();
}
