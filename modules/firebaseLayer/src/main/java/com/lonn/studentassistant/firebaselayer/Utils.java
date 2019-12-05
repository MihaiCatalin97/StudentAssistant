package com.lonn.studentassistant.firebaselayer;

import android.util.Log;

import androidx.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
