package com.lonn.studentassistant.validation.validators;

import com.lonn.studentassistant.validation.ValidationResult;
import com.lonn.studentassistant.validation.functionalInterfaces.Predicate;

class ValidationRule<T> {
    private Predicate<T> predicate;
    private String errorMessage;

    public static <T> ValidationRule<T> test(Predicate<T> predicate) {
        ValidationRule<T> rule = new ValidationRule<>();
        rule.predicate = predicate;
        return rule;
    }

    public ValidationRule<T> orError(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    ValidationResult validate(T entity) {
        if (predicate.test(entity)) {
            return ValidationResult.valid();
        }
        return ValidationResult.invalid(errorMessage);
    }
}
