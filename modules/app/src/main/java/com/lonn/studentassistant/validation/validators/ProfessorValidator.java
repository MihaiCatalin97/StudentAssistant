package com.lonn.studentassistant.validation.validators;

import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidEmail;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidName;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidPhoneNumber;

public class ProfessorValidator extends Validator<ProfessorViewModel> {
	private static List<ValidationRule<ProfessorViewModel>> validationRules;

	static {
		validationRules = new ArrayList<>();

		validationRules.add(ValidationRule.test(isValidName(ProfessorViewModel::getFirstName))
				.orError("Invalid first name"));
		validationRules.add(ValidationRule.test(isValidName(ProfessorViewModel::getLastName))
				.orError("Invalid last name"));
		validationRules.add(ValidationRule.test(isValidEmail(ProfessorViewModel::getEmail))
				.orError("Invalid email"));
		validationRules.add(ValidationRule.test(isValidPhoneNumber(ProfessorViewModel::getPhoneNumber))
				.orError("Invalid phone number"));
	}

	protected List<ValidationRule<ProfessorViewModel>> getValidationRules() {
		return validationRules;
	}
}
