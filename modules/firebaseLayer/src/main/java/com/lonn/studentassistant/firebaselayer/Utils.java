package com.lonn.studentassistant.firebaselayer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.lonn.studentassistant.firebaselayer.entities.enums.WeekDay;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

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

	public static String generateHashDigest(String stringToHash) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(stringToHash.getBytes());
			byte[] digest = md.digest();

			StringBuilder stringBuilder = new StringBuilder();
			for (byte digestByte : digest) {
				stringBuilder.append(Integer.toHexString((digestByte & 0xFF) | 0x100).substring(1, 3));
			}

			return stringBuilder.toString();
		}
		catch (NoSuchAlgorithmException | NullPointerException e) {
			Log.e("Error while hashing", e.toString());
			return null;
		}
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

	public static String dayToString(int day) {
		WeekDay weekDay = WeekDay.getByInt(day);

		if (weekDay != null) {
			return weekDay.getDayStringEng();
		}
		return null;
	}

	public static String hourToString(int hour) {
		String minuteString = Integer.toString(hour % 100);
		String hourString = Integer.toString(hour / 100);

		minuteString = padLeftWithZeros(minuteString);
		hourString = padLeftWithZeros(hourString);

		return hourString + ":" + minuteString;
	}

	private static String padLeftWithZeros(String stringToPad) {
		if (stringToPad.length() == 0) {
			return "00";
		}
		else if (stringToPad.length() == 1) {
			return "0" + stringToPad;
		}
		return stringToPad;
	}
}
