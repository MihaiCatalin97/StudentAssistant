package com.lonn.studentassistant.validation.validators;

import com.lonn.studentassistant.firebaselayer.models.Student;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.validation.predicates.IntegerPredicates.isValidYear;
import static com.lonn.studentassistant.validation.predicates.StringPredicates.isValidEmail;
import static com.lonn.studentassistant.validation.predicates.StringPredicates.isValidGroup;
import static com.lonn.studentassistant.validation.predicates.StringPredicates.isValidInitial;
import static com.lonn.studentassistant.validation.predicates.StringPredicates.isValidName;
import static com.lonn.studentassistant.validation.predicates.StringPredicates.isValidPhoneNumber;
import static com.lonn.studentassistant.validation.predicates.StringPredicates.nonEmptyStringPredicate;

public class StudentValidator extends Validator<Student> {
    private static List<ValidationRule<Student>> validationRules;

    static {
        validationRules = new ArrayList<>();

        validationRules.add(ValidationRule.test(nonEmptyStringPredicate(Student::getStudentId))
                .orError("Invalid student id"));
        validationRules.add(ValidationRule.test(isValidName(Student::getFirstName))
                .orError("Invalid first name"));
        validationRules.add(ValidationRule.test(isValidName(Student::getLastName))
                .orError("Invalid last name"));
        validationRules.add(ValidationRule.test(isValidInitial(Student::getFatherInitial))
                .orError("Invalid father's initial"));
        validationRules.add(ValidationRule.test(isValidEmail(Student::getEmail))
                .orError("Invalid email"));
        validationRules.add(ValidationRule.test(isValidPhoneNumber(Student::getPhoneNumber))
                .orError("Invalid phone number"));
        validationRules.add(ValidationRule.test(isValidYear(Student::getYear))
                .orError("Invalid year"));
        validationRules.add(ValidationRule.test(isValidGroup(Student::getGroup))
                .orError("Invalid group"));
    }

    protected List<ValidationRule<Student>> getValidationRules() {
        return validationRules;
    }
}
