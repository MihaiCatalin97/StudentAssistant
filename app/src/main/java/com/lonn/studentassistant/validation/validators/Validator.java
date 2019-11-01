package com.lonn.studentassistant.validation.validators;

import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.validation.ValidationResult;

import java.util.List;

public abstract class Validator<T extends BaseEntity> {
    public ValidationResult validate(T entity) {
        for (ValidationRule<T> rule : getValidationRules()) {
            ValidationResult validationResult = rule.validate(entity);

            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        return ValidationResult.valid();
    }

    protected abstract List<ValidationRule<T>> getValidationRules();
}
