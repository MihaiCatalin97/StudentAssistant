package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.entities.IdentificationHash;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class CredentialsCheckRequest extends Request<IdentificationHash, Exception> {
	private String identificationHash;

	public CredentialsCheckRequest onSuccess(Consumer<IdentificationHash> onSuccess) {
		this.onSuccess = onSuccess;
		return this;
	}

	public CredentialsCheckRequest onError(Consumer<Exception> onError) {
		this.onError = onError;
		return this;
	}

	public Consumer<IdentificationHash> onSuccess() {
		return onSuccess;
	}

	public Consumer<Exception> onError() {
		return onError;
	}
}
