package com.lonn.studentassistant.validation.validators;

import com.lonn.studentassistant.firebaselayer.entities.Administrator;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.validation.predicates.StringPredicates.isValidName;
import static com.lonn.studentassistant.validation.predicates.StringPredicates.nonEmptyStringPredicate;

public class AdministratorValidator extends Validator<Administrator> {
    private static List<ValidationRule<Administrator>> validationRules;

    static {
        validationRules = new ArrayList<>();

        validationRules.add(ValidationRule.test(isValidName(Administrator::getFirstName))
                .orError("Invalid first name"));
        validationRules.add(ValidationRule.test(isValidName(Administrator::getLastName))
                .orError("Invalid last name"));
        validationRules.add(ValidationRule.test(nonEmptyStringPredicate(Administrator::getAdministratorKey))
                .orError("Invalid administrator key"));
    }

    protected List<ValidationRule<Administrator>> getValidationRules() {
        return validationRules;
    }
}
