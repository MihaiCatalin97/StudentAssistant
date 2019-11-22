package com.lonn.studentassistant.validation;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Regex {
    public static final String PASSWORD_REGEX = "(.){1,}";
    public static final String PHONE_NUMBER_REGEX = "\\+?[0-9]{10,12}";
    public static final String GROUP_REGEX = "[ABEX][1-8]";
    public static final String INITIAL_REGEX = "([A-Z](\\.)?)+";
    private static final String ONE_WORD_NAME_REGEX = "([A-Z][a-z]+)";
    public static final String NAME_REGEX = ONE_WORD_NAME_REGEX + "([ \\-]" + ONE_WORD_NAME_REGEX + ")*";
    private static final String ALPHA_NUMERIC_STRING_REGEX = "[a-zA-Z0-9_\\.]+";
    public static final String EMAIL_REGEX = ALPHA_NUMERIC_STRING_REGEX + "@" + ALPHA_NUMERIC_STRING_REGEX + "\\." +
            ALPHA_NUMERIC_STRING_REGEX;
}
