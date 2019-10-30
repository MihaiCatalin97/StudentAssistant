package com.lonn.studentassistant.firebaselayer;

import androidx.annotation.NonNull;

public class Utils {

    public static String emailToKey(String email)
    {
        return email.replace(".",",");
    }

    public static String stringToFirstCapital(@NonNull String stringToFormat) {
        StringBuilder result = new StringBuilder();
        String[] words = stringToFormat.split("[_-]");

        for (String word : words) {
            result.append(word.substring(0, 1).toUpperCase())
                    .append(word.substring(1).toLowerCase());
        }

        return result.toString();
    }

    public static String padWithZeroesToSize(String toPad) {
        StringBuilder result = new StringBuilder();
        int numberOfChars = 100 - (toPad == null ? 0 : toPad.length());

        for (int i = 0; i < numberOfChars; i++) {
            result.append("0");
        }

        result.append(toPad);

        return result.toString();
    }
}
