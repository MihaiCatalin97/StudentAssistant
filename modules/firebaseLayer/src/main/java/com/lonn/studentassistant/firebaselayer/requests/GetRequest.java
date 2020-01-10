package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class GetRequest<T extends BaseEntity, V extends Throwable> extends Request<List<T>, V> {
	private DatabaseTable<T> databaseTable;
	private Boolean subscribe = true;
	private Predicate<? super T> predicate;

	public GetRequest<T, V> onSuccess(Consumer<List<T>> onSuccess) {
		this.onSuccess = onSuccess;
		return this;
	}

	public GetRequest<T, V> onError(Consumer<V> onError) {
		this.onError = onError;
		return this;
	}

	public Consumer<List<T>> onSuccess() {
		return onSuccess;
	}

	public Consumer<V> onError() {
		return onError;
	}
}
