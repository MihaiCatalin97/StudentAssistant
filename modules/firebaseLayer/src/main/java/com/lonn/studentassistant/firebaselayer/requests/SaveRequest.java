package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class SaveRequest<T extends BaseEntity, V extends Throwable> extends Request<Void, V> {
	private DatabaseTable databaseTable;
	private List<T> entities = new ArrayList<>();

	public SaveRequest<T, V> onSuccess(Consumer<Void> onSuccess) {
		this.onSuccess = onSuccess;
		return this;
	}

	public SaveRequest<T, V> onError(Consumer<V> onError) {
		this.onError = onError;
		return this;
	}

	public SaveRequest<T, V> entity(T entity) {
		if (entity != null) {
			this.entities = new ArrayList<>();
			entities.add(entity);
		}
		return this;
	}

	public Consumer<Void> onSuccess() {
		return onSuccess;
	}

	public Consumer<V> onError() {
		return onError;
	}
}
