package com.lonn.studentassistant.validation.validators;

import com.lonn.studentassistant.firebaselayer.viewModels.AdministratorViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidEmail;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidName;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidPhoneNumber;

public class AdministratorValidator extends Validator<AdministratorViewModel> {
	private static List<ValidationRule<AdministratorViewModel>> validationRules;

	static {
		validationRules = new ArrayList<>();

		validationRules.add(ValidationRule.test(isValidName(AdministratorViewModel::getFirstName))
				.orError("Invalid first name"));
		validationRules.add(ValidationRule.test(isValidName(AdministratorViewModel::getLastName))
				.orError("Invalid last name"));
		validationRules.add(ValidationRule.test(isValidEmail(AdministratorViewModel::getEmail))
				.orError("Invalid email"));
		validationRules.add(ValidationRule.test(isValidPhoneNumber(AdministratorViewModel::getPhoneNumber))
				.orError("Invalid phone number"));
	}

	protected List<ValidationRule<AdministratorViewModel>> getValidationRules() {
		return validationRules;
	}
}
