package com.lonn.studentassistant.utils;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OtherActivityViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.YYYY");
    public static int displayHeight, displayWidth;

    public static String hideStudentId(String studentId) {
        if (studentId.length() > 4) {
            return padLeft(studentId.substring(studentId.length() - 4), '*', studentId.length());
        }
        return studentId;
    }

    public static String dateToStringDate(Date date) {
        if (date != null) {
            return DATE_FORMATTER.format(date);
        }
        return null;
    }

    public static String yearToString(int year) {
        if (year == 1) {
            return "First year";
        }
        if (year == 2) {
            return "Second year";
        }
        if (year == 3) {
            return "Third year";
        }
        if (year == 4) {
            return "Master First year";
        }
        if (year == 5) {
            return "Master Second year";
        }

        return "Unknown year";
    }

    public static String semesterToString(int semester) {
        if (semester == 1) {
            return "First semester";
        }
        if (semester == 2) {
            return "Second semester";
        }

        return "Unknown semester";
    }

    public static List<View> getVisibleChildren(ViewGroup v) {
        List<View> result = new LinkedList<>();

        for (int i = 0; i < v.getChildCount(); i++) {
            View child = v.getChildAt(i);

            if (child.getVisibility() == View.VISIBLE) {
                result.add(child);
            }
        }

        return result;
    }

    public static void hideViews(List<View> views) {
        for (View visibleView : views) {
            visibleView.setVisibility(View.INVISIBLE);
        }
    }

    public static void init(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        displayWidth = size.x;
        displayHeight = size.y;
    }

    public static String hourToString(int hour) {
        String minuteString = Integer.toString(hour % 100);
        String hourString = Integer.toString(hour / 100);

        minuteString = padLeft(minuteString, '0', 2);
        hourString = padLeft(hourString, '0', 2);

        return hourString + ":" + minuteString;
    }

    public static String padLeft(String stringToPad, char paddingCharacter, int desiredSize) {
        StringBuilder stringBuilder = new StringBuilder(desiredSize);

        for (int i = 0; i < desiredSize - stringToPad.length(); i++) {
            stringBuilder.append(paddingCharacter);
        }

        stringBuilder.append(stringToPad);

        return stringBuilder.toString();
    }

    public static int getDefaultPersonImageForName(String name) {
        if (name == null) {
            return R.drawable.default_person_image_male;
        }

        if (name.length() > 0 && name.charAt(name.length() - 1) == 'a') {
            return R.drawable.default_person_image_female;
        }

        return R.drawable.default_person_image_male;
    }

    public static Collection<CourseViewModel> getCoursesWithoutProfessors(Collection<CourseViewModel> disciplines) {
        Collection<CourseViewModel> disciplinesWithoutProfessors = new ArrayList<>();

        if (disciplines != null) {
            for (CourseViewModel discipline : disciplines) {
                if (discipline.getProfessors() == null || discipline.getProfessors().size() == 0) {
                    disciplinesWithoutProfessors.add(discipline);
                }
            }
        }

        return disciplinesWithoutProfessors;
    }

    public static Collection<OtherActivityViewModel> getActivitiesWithoutProfessors(Collection<OtherActivityViewModel> disciplines) {
        Collection<OtherActivityViewModel> disciplinesWithoutProfessors = new ArrayList<>();

        if (disciplines != null) {
            for (OtherActivityViewModel discipline : disciplines) {
                if (discipline.getProfessors() == null || discipline.getProfessors().size() == 0) {
                    disciplinesWithoutProfessors.add(discipline);
                }
            }
        }

        return disciplinesWithoutProfessors;
    }
}
