package com.lonn.studentassistant.firebaselayer.services.abstractions;

import android.os.Handler;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseListTask;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseTask;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseVoidTask;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.DeleteAllRequest;
import com.lonn.studentassistant.firebaselayer.requests.DeleteByIdRequest;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;
import com.lonn.studentassistant.firebaselayer.services.AuthenticationService;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.List;

import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.READ_PUBLIC;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE_ADD_AGGREGATED;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public abstract class Service<T extends BaseEntity, V extends Exception, U extends EntityViewModel<T>> {
	protected FirebaseConnection firebaseConnection;
	protected AuthenticationService authenticationService;
	protected ViewModelAdapter<T, U> adapter;

	protected Service(FirebaseConnection firebaseConnection) {
		this.firebaseConnection = firebaseConnection;
	}

	protected void init() {
		authenticationService = AuthenticationService.getInstance(firebaseConnection);
	}

	public FirebaseTask<List<U>, V> getAll() {
		return new FirebaseListTask<T, U, V>(firebaseConnection,
				new GetRequest<T, V>().databaseTable(getDatabaseTable()))
				.setTransformer(this::transform);
	}

	public Future<U, Exception> getById(String id, boolean subscribe) {
		Future<U, Exception> result = new Future<>();

		firebaseConnection.execute(new GetRequest<T, V>()
				.databaseTable(getDatabaseTable())
				.predicate(where(ID).equalTo(id))
				.subscribe(subscribe)
				.onSuccess(entities -> {
					if (entities != null && entities.size() == 1) {
						if (getPermissionLevel(entities.get(0)).isAtLeast(READ_PUBLIC)) {
							result.complete(transform(entities.get(0)));
						}
						else {
							result.completeExceptionally(new Exception("Insufficient privileges"));
						}
					}
					else {
						result.completeExceptionally(new Exception("Entity not found"));
					}
				}));

		return result;
	}

	public Future<Void, Exception> save(U entityViewModel) {
		Future<Void, Exception> result = new Future<>();

		if (getPermissionLevel(adapter.adapt(entityViewModel)).isAtLeast(WRITE_ADD_AGGREGATED)) {
			firebaseConnection.execute(new SaveRequest<>()
					.databaseTable(getDatabaseTable())
					.entity(adapter.adapt(entityViewModel))
					.onSuccess(result::complete)
					.onError(result::completeExceptionally));
		}
		else {
			new Handler().postDelayed(() ->
							result.completeExceptionally(new Exception("Insufficient privileges")),
					500);
		}

		return result;
	}

	public FirebaseTask<Void, V> save(T entity) {
		if (getPermissionLevel(entity).isAtLeast(WRITE)) {
			return new FirebaseVoidTask<>(firebaseConnection,
					new SaveRequest<T, V>()
							.databaseTable(getDatabaseTable())
							.entity(entity));
		}
		else {
			return null;
		}
	}

	public FirebaseTask<Void, Exception> deleteAll() {
		return new FirebaseVoidTask<>(firebaseConnection,
				new DeleteAllRequest()
						.databaseTable(getDatabaseTable()));
	}

	public Future<Void, Exception> deleteById(String id) {
		Future<Void, Exception> result = new Future<>();

		getById(id, false)
				.onSuccess(entity -> {
					if (getPermissionLevel(adapter.adapt(entity)).isAtLeast(WRITE)) {
						firebaseConnection.execute(new DeleteByIdRequest()
								.databaseTable(getDatabaseTable())
								.key(id)
								.onSuccess(result::complete)
								.onError(result::completeExceptionally));
					}
					else {
						result.completeExceptionally(new Exception("Insufficient privileges"));
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	protected U transform(T entity) {
		return adapter.adapt(entity);
	}

	protected List<U> transform(List<T> entities) {
		return adapter.adapt(entities);
	}

	protected abstract DatabaseTable<T> getDatabaseTable();

	protected abstract PermissionLevel getPermissionLevel(T entity);
}
