package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class DeleteAllRequest extends Request<Void, Exception> {
	private DatabaseTable databaseTable;

	public DeleteAllRequest onSuccess(Consumer<Void> onSuccess) {
		this.onSuccess = onSuccess;
		return this;
	}

	public DeleteAllRequest onError(Consumer<Exception> onError) {
		this.onError = onError;
		return this;
	}

	public Consumer<Void> onSuccess() {
		return onSuccess;
	}

	public Consumer<Exception> onError() {
		return onError;
	}
}
