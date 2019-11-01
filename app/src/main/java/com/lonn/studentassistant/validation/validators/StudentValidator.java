package com.lonn.studentassistant.validation.validators;

import com.lonn.studentassistant.firebaselayer.models.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentValidator extends Validator<Student> {
    private static List<ValidationRule<Student>> validationRules;

    static {
        validationRules = new ArrayList<>();

        validationRules.add(new ValidationRule<>((student) -> student.getStudentId() != null,
                "Invalid Student Id"));
    }

    protected List<ValidationRule<Student>> getValidationRules() {
        return validationRules;
    }
}
