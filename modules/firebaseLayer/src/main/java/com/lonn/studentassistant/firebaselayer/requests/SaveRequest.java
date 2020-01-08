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
public class SaveRequest<T extends BaseEntity> extends Request<Void, Exception> {
	private DatabaseTable databaseTable;
	private List<T> entities = new ArrayList<>();

	public SaveRequest<T> onSuccess(Consumer<Void> onSuccess) {
		this.onSuccess = onSuccess;
		return this;
	}

	public SaveRequest<T> onError(Consumer<Exception> onError) {
		this.onError = onError;
		return this;
	}

	public SaveRequest<T> entity(T entity) {
		if (entity != null) {
			this.entities = new ArrayList<>();
			entities.add(entity);
		}
		return this;
	}

	public Consumer<Void> onSuccess() {
		return onSuccess;
	}

	public Consumer<Exception> onError() {
		return onError;
	}
}
