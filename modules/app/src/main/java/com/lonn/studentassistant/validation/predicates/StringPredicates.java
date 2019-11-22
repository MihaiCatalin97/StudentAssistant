package com.lonn.studentassistant.validation.predicates;

import com.lonn.studentassistant.validation.Regex;
import com.lonn.studentassistant.validation.functionalInterfaces.Predicate;
import com.lonn.studentassistant.validation.functionalInterfaces.StringGetter;

public class StringPredicates {
    public static <T> Predicate<T> nonEmptyStringPredicate(StringGetter<T> getter) {
        return (entity) -> !getter.getStringField(entity).isEmpty();
    }

    public static <T> Predicate<T> isValidEmail(StringGetter<T> getter) {
        return nonEmptyAndMatchesRegex(getter, Regex.EMAIL_REGEX);
    }

    public static <T> Predicate<T> isValidPhoneNumber(StringGetter<T> getter) {
        return nonEmptyAndMatchesRegex(getter, Regex.PHONE_NUMBER_REGEX);
    }

    public static <T> Predicate<T> isValidInitial(StringGetter<T> getter) {
        return nonEmptyAndMatchesRegex(getter, Regex.INITIAL_REGEX);
    }

    public static <T> Predicate<T> isValidName(StringGetter<T> getter) {
        return nonEmptyAndMatchesRegex(getter, Regex.NAME_REGEX);
    }

    public static <T> Predicate<T> isValidPassword(StringGetter<T> getter) {
        return nonEmptyAndMatchesRegex(getter, Regex.PASSWORD_REGEX);
    }

    public static <T> Predicate<T> equalsMatchingCase(StringGetter<T> getter,
                                                      StringGetter<T> valueToMatch) {
        return nonEmptyAndEqualsMatchingCase(getter, valueToMatch);
    }

    public static <T> Predicate<T> isValidGroup(StringGetter<T> getter) {
        return nonEmptyAndMatchesRegex(getter, Regex.GROUP_REGEX);
    }

    private static <T> Predicate<T> nonEmptyAndEqualsMatchingCase(StringGetter<T> getter,
                                                                  StringGetter<T> valueToMatch) {
        return (entity) -> !getter.getStringField(entity).isEmpty() &&
                getter.getStringField(entity)
                        .equals(valueToMatch.getStringField(entity));
    }

    private static <T> Predicate<T> nonEmptyAndMatchesRegex(StringGetter<T> getter,
                                                            String regex) {
        return (entity) -> !getter.getStringField(entity).isEmpty()
                && getter.getStringField(entity).matches(regex);
    }
}
