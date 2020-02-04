package com.lonn.studentassistant.validation.predicates;

import com.lonn.studentassistant.functionalIntefaces.Function;
import com.lonn.studentassistant.functionalIntefaces.Predicate;

import static com.lonn.studentassistant.validation.Regex.EMAIL_REGEX;
import static com.lonn.studentassistant.validation.Regex.INITIAL_REGEX;
import static com.lonn.studentassistant.validation.Regex.NAME_REGEX;
import static com.lonn.studentassistant.validation.Regex.PASSWORD_REGEX;
import static com.lonn.studentassistant.validation.Regex.PHONE_NUMBER_REGEX;

public class StringValidationPredicates {
	public static <T> Predicate<T> nonEmptyStringPredicate(Function<T, String> getter) {
		return (entity) -> !getter.apply(entity).isEmpty();
	}

	public static <T> Predicate<T> isValidEmail(Function<T, String> getter) {
		return emptyOrMatchesRegex(getter, EMAIL_REGEX);
	}

	public static <T> Predicate<T> isValidPhoneNumber(Function<T, String> getter) {
		return emptyOrMatchesRegex(getter, PHONE_NUMBER_REGEX);
	}

	public static <T> Predicate<T> isValidInitial(Function<T, String> getter) {
		return emptyOrMatchesRegex(getter, INITIAL_REGEX);
	}

	public static <T> Predicate<T> isValidName(Function<T, String> getter) {
		return nonEmptyAndMatchesRegex(getter, NAME_REGEX);
	}

	public static <T> Predicate<T> isValidPassword(Function<T, String> getter) {
		return nonEmptyAndMatchesRegex(getter, PASSWORD_REGEX);
	}

	public static <T> Predicate<T> equalsMatchingCase(Function<T, String> getter,
													  Function<T, String> valueToMatch) {
		return nonEmptyAndEqualsMatchingCase(getter, valueToMatch);
	}

	public static Boolean isValidName(String fullName) {
		return nonEmptyAndMatchesRegex(fullName, NAME_REGEX);
	}

	public static Boolean isValidEmail(String email) {
		return emptyOrMatchesRegex(email, EMAIL_REGEX);
	}

	public static Boolean isValidPhoneNumber(String phoneNumber) {
		return emptyOrMatchesRegex(phoneNumber, PHONE_NUMBER_REGEX);
	}

	private static <T> Predicate<T> nonEmptyAndEqualsMatchingCase(Function<T, String> getter,
																  Function<T, String> valueToMatch) {
		return (entity) -> !getter.apply(entity).isEmpty() &&
				getter.apply(entity)
						.equals(valueToMatch.apply(entity));
	}

	private static <T> Predicate<T> nonEmptyAndMatchesRegex(Function<T, String> getter,
															String regex) {
		return (entity) -> getter.apply(entity) != null && !getter.apply(entity).isEmpty()
				&& getter.apply(entity).matches(regex);
	}

	private static <T> Predicate<T> emptyOrMatchesRegex(Function<T, String> getter,
														String regex) {
		return (entity) -> getter.apply(entity) == null || getter.apply(entity).isEmpty()
				|| getter.apply(entity).matches(regex);
	}

	private static Boolean emptyOrMatchesRegex(String value,
											   String regex) {
		return value == null || value.isEmpty() || value.matches(regex);
	}

	private static Boolean nonEmptyAndMatchesRegex(String value,
												   String regex) {
		return value != null && !value.isEmpty()
				&& value.matches(regex);
	}
}
