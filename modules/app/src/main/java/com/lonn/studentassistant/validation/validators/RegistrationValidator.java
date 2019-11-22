package com.lonn.studentassistant.validation.validators;

import com.lonn.studentassistant.activities.implementations.register.RegistrationInformation;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.validation.predicates.StringPredicates.equalsMatchingCase;
import static com.lonn.studentassistant.validation.predicates.StringPredicates.isValidEmail;
import static com.lonn.studentassistant.validation.predicates.StringPredicates.isValidPassword;

public class RegistrationValidator extends Validator<RegistrationInformation> {
    private static List<ValidationRule<RegistrationInformation>> validationRules;

    static {
        validationRules = new ArrayList<>();

        validationRules.add(ValidationRule.test(isValidEmail(RegistrationInformation::getEmail))
                .orError("Invalid email"));
        validationRules.add(ValidationRule.test(isValidPassword(RegistrationInformation::getPassword))
                .orError("Invalid password"));
        validationRules.add(ValidationRule.test(equalsMatchingCase(
                RegistrationInformation::getPassword,
                RegistrationInformation::getRepeatPassword))
                .orError("Passwords do not match"));
    }

    protected List<ValidationRule<RegistrationInformation>> getValidationRules() {
        return validationRules;
    }
}