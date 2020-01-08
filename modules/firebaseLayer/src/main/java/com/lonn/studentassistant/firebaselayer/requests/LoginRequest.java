package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class LoginRequest extends Request<Void, Exception> {
	private String username;
	private String password;

	public LoginRequest onSuccess(Consumer<Void> onSuccess) {
		this.onSuccess = onSuccess;
		return this;
	}

	public LoginRequest onError(Consumer<Exception> onError) {
		this.onError = onError;
		return this;
	}

	public Consumer<Void> onSuccess(){
		return onSuccess;
	}

	public Consumer<Exception> onError(){
		return onError;
	}
}
