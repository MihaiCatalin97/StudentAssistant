package com.lonn.studentassistant.common;

import android.util.Base64;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{
    public static String encrypt(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    public static String emailToKey(String email)
    {
        return email.replace(".",",");
    }

    public static String keyToEmail(String key)
    {
        return key.replace(",",".");
    }

    public static boolean isValidStudentId(String studentId)
    {
        String regex = "^[a-zA-Z0-9]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(studentId);

        return m.matches();
    }
}
