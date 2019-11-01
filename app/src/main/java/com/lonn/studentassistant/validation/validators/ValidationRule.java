package com.lonn.studentassistant.validation.validators;

import com.lonn.studentassistant.common.Predicate;
import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.validation.ValidationResult;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class ValidationRule<T extends BaseEntity> {
    private Predicate<T> predicate;
    private String errorMessage;

    ValidationResult validate(T entity) {
        if (predicate.test(entity)) {
            return ValidationResult.valid();
        }
        return ValidationResult.invalid(errorMessage);
    }
}
