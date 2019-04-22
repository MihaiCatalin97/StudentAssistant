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

    static int groupToYear(String group)
    {
        if(group.startsWith("III"))
            return 3;
        if(group.startsWith("II"))
            return 2;
        return 1;
    }

    static String getTagValue(String tag)
    {
        if(tag.split(">").length > 1)
            return tag.split(">")[1].split("<")[0].trim();
        return tag.split(">")[0].split("<")[0].trim();
    }

    static int dayToInt(String day)
    {
        switch (day)
        {
            case "Monday":
            case "Luni":
            {
                return 1;
            }
            case "Tuesday":
            case "Marti":
            {
                return 2;
            }
            case "Wednesday":
            case "Miercuri":
            {
                return 3;
            }
            case "Thursday":
            case "Joi":
            {
                return 4;
            }
            case "Friday":
            case "Vineri":
            {
                return 5;
            }
            case "Saturday":
            case "Sambata":
            {
                return 6;
            }
            default:
            {
                return 0;
            }
        }
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
