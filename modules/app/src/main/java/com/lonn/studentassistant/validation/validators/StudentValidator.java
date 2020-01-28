package com.lonn.studentassistant.validation.validators;

import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.validation.predicates.MiscValidationPredicates.isValidCycleSpecialization;
import static com.lonn.studentassistant.validation.predicates.MiscValidationPredicates.isValidGroupForCycleSpecializationYear;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidEmail;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidInitial;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidName;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidPhoneNumber;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.nonEmptyStringPredicate;

public class StudentValidator extends Validator<StudentViewModel> {
	private static List<ValidationRule<StudentViewModel>> validationRules;

	static {
		validationRules = new ArrayList<>();

		validationRules.add(ValidationRule.test(nonEmptyStringPredicate(StudentViewModel::getStudentId))
				.orError("Invalid student id"));
		validationRules.add(ValidationRule.test(isValidName(StudentViewModel::getFirstName))
				.orError("Invalid first name"));
		validationRules.add(ValidationRule.test(isValidName(StudentViewModel::getLastName))
				.orError("Invalid last name"));
		validationRules.add(ValidationRule.test(isValidCycleSpecialization(StudentViewModel::getCycleSpecializationYear))
				.orError("Invalid year"));
		validationRules.add(ValidationRule.test(isValidGroupForCycleSpecializationYear(StudentViewModel::getCycleSpecializationYear,
				StudentViewModel::getGroup))
				.orError("Invalid group"));
		validationRules.add(ValidationRule.test(isValidEmail(StudentViewModel::getEmail))
				.orError("Invalid email"));
		validationRules.add(ValidationRule.test(isValidInitial(StudentViewModel::getFatherInitial))
				.orError("Invalid father's initial"));
		validationRules.add(ValidationRule.test(isValidPhoneNumber(StudentViewModel::getPhoneNumber))
				.orError("Invalid phone number"));
	}

	protected List<ValidationRule<StudentViewModel>> getValidationRules() {
		return validationRules;
	}
}
