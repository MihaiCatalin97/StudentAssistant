package com.lonn.studentassistant.utils;

import android.app.Activity;
import android.graphics.Point;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.firebaselayer.entities.enums.WeekDay;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static int displayHeight, displayWidth;
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.YYYY");

	public static String hideStudentId(String studentId) {
		return padLeft(studentId.substring(studentId.length() - 4), '*', studentId.length());
	}

	public static String dateToStringDate(Date date) {
		if (date != null) {
			return DATE_FORMATTER.format(date);
		}
		return null;
	}

	public static String encrypt(String input) {
		return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
	}

	public static String decrypt(String input) {
		return new String(Base64.decode(input, Base64.DEFAULT));
	}

	public static String dayToString(int day) {
		WeekDay weekDay = WeekDay.getByInt(day);

		if (weekDay != null) {
			return weekDay.getDayStringEng();
		}
		return null;
	}

	public static int groupToYear(String group) {
		if (group.startsWith("I3") || group.startsWith("III")) {
			return 3;
		}
		if (group.startsWith("I2") || group.startsWith("II")) {
			return 2;
		}
		if (group.startsWith("I1") || group.startsWith("I")) {
			return 1;
		}
		if (group.startsWith("M")) {
			if (group.endsWith("2")) {
				return 5;
			}
			return 4;
		}
		return 1;
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

	public static int getId(String resourceName, Class<?> c) {
		try {
			Field idField = c.getDeclaredField(resourceName);
			return idField.getInt(idField);
		}
		catch (Exception e) {
			Log.e("Error getId", e.getLocalizedMessage());
		}
		return 0;
	}

	public static int dayToInt(String day) {
		WeekDay weekDay = WeekDay.getByString(day);

		if (weekDay != null) {
			return weekDay.getDayInt();
		}
		return 0;
	}

	public static List<String> getStudentGroupTags(Student student) {
		List<String> result = new LinkedList<>();

		result.add("I" + student.getCycleSpecializationYear().getYear());
		result.add(result.get(0) + student.getGroup());

		String semian = student.getGroup().substring(0, 1);

		if (semian.equals("A") || semian.equals("B")) {
			result.add(result.get(0) + student.getGroup().substring(0, 1));
		}

		return result;
	}

	public static boolean isValidStudentId(String studentId) {
		String regex = "^[a-zA-Z0-9]+$";

		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(studentId);

		return m.matches();
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

	public static void removeMargins(View view) {
		if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
			p.setMargins(0, 0, 0, 0);
			view.requestLayout();
		}

		view.setPadding(0, 0, 0, 0);
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
		if (name == null)
			return R.drawable.default_person_image_male;

		if (name.charAt(name.length() - 1) == 'a') {
			return R.drawable.default_person_image_female;
		}

		return R.drawable.default_person_image_male;
	}
}
