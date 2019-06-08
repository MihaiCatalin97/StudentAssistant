package com.lonn.studentassistant.common;

import android.app.Activity;
import android.graphics.Point;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{
    public static int displayHeight, displayWidth;

    public static String encrypt(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    static int groupToYear(String group)
    {
        if(group.startsWith("I3") || group.startsWith("III"))
            return 3;
        if(group.startsWith("I2") || group.startsWith("II"))
            return 2;
        if(group.startsWith("I1") || group.startsWith("I"))
            return 1;
        if(group.startsWith("M"))
        {
            if(group.endsWith("2"))
                return 5;
            return 4;
        }
        return 1;
    }

    public static String yearToString(int year)
    {
        if (year == 1)
            return "First year";
        if (year == 2)
            return "Second year";
        if (year == 3)
            return "Third year";

        return "Unknown year";
    }

    public static String semesterToString(int semester)
    {
        if (semester == 1)
            return "First semester";
        if (semester == 2)
            return "Second semester";

        return "Unknown semester";
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

    public static List<View> getVisibleChildren(ViewGroup v)
    {
        List<View> result = new LinkedList<>();

        for (int i=0;i<v.getChildCount();i++)
        {
            View child = v.getChildAt(i);

            if(child.getVisibility() == View.VISIBLE)
                result.add(child);
        }

        return result;
    }

    public static void hideViews(List<View> views)
    {
        for(View visibleView : views)
        {
            visibleView.setVisibility(View.INVISIBLE);
        }
    }

    public static void init(Activity activity)
    {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        displayWidth = size.x;
        displayHeight = size.y;
    }
}
