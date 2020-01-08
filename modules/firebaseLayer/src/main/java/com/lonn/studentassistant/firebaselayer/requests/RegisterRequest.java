package com.lonn.studentassistant.firebaselayer.requests;

import com.google.firebase.auth.FirebaseUser;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class RegisterRequest extends Request<FirebaseUser, Exception> {
	private String email;
	private String password;
	private String personUUID;

	public RegisterRequest onSuccess(Consumer<FirebaseUser> onSuccess) {
		this.onSuccess = onSuccess;
		return this;
	}

	public RegisterRequest onError(Consumer<Exception> onError) {
		this.onError = onError;
		return this;
	}

	public Consumer<FirebaseUser> onSuccess() {
		return onSuccess;
	}

	public Consumer<Exception> onError() {
		return onError;
	}
}
