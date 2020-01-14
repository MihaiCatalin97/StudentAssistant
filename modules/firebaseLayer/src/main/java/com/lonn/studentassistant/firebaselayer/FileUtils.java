package com.lonn.studentassistant.firebaselayer;

public class FileUtils {
	public static String sizeToString(long size) {
		double result = size;
		int sizeCounter = 0;

		while (result > 1024) {
			result /= 1024;
			sizeCounter++;
		}

		return String.format("%.2f", result) + " " + sizeCounterToString(sizeCounter);
	}

	public static String sizeCounterToString(int sizeCounter) {
		switch (sizeCounter) {
			case 1: {
				return "KB";
			}
			case 2: {
				return "MB";
			}
			default: {
				return "B";
			}
		}
	}

	public static String getExtensionFromMime(String mime) {
		return mime.split("/")[1];
	}
}
