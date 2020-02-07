package com.lonn.studentassistant.validation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationResult {
	private boolean isValid;
	private String errorMessage;

	public static ValidationResult invalid(String errorMessage) {
		return new ValidationResult(false, errorMessage);
	}

	public static ValidationResult valid() {
		return new ValidationResult(true, null);
	}
}
