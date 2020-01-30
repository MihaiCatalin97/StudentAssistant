package com.lonn.studentassistant.validation.predicates;

import com.lonn.studentassistant.functionalIntefaces.Function;
import com.lonn.studentassistant.functionalIntefaces.Predicate;
import com.lonn.studentassistant.validation.Regex;

import static com.lonn.studentassistant.validation.Regex.EMAIL_REGEX;

public class StringValidationPredicates {
	public static <T> Predicate<T> nonEmptyStringPredicate(Function<T, String> getter) {
		return (entity) -> !getter.apply(entity).isEmpty();
	}

	public static <T> Predicate<T> isValidEmail(Function<T, String> getter) {
		return emptyOrMatchesRegex(getter, EMAIL_REGEX);
	}

	public static <T> Predicate<T> isValidPhoneNumber(Function<T, String> getter) {
		return emptyOrMatchesRegex(getter, Regex.PHONE_NUMBER_REGEX);
	}

	public static <T> Predicate<T> isValidInitial(Function<T, String> getter) {
		return emptyOrMatchesRegex(getter, Regex.INITIAL_REGEX);
	}

	public static <T> Predicate<T> isValidName(Function<T, String> getter) {
		return nonEmptyAndMatchesRegex(getter, Regex.NAME_REGEX);
	}

	public static <T> Predicate<T> isValidPassword(Function<T, String> getter) {
		return nonEmptyAndMatchesRegex(getter, Regex.PASSWORD_REGEX);
	}

	public static <T> Predicate<T> equalsMatchingCase(Function<T, String> getter,
													  Function<T, String> valueToMatch) {
		return nonEmptyAndEqualsMatchingCase(getter, valueToMatch);
	}

	public static Boolean isValidEmail(String email) {
		return emptyOrMatchesRegex(email, EMAIL_REGEX);
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
}
