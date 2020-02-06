package com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums;

import androidx.annotation.NonNull;

public enum WeekDay {
	MONDAY("Monday", "Luni", 1),
	TUESDAY("Tuesday", "Marti", 2),
	WEDNESDAY("Wednesday", "Miercuri", 3),
	THURSDAY("Thursday", "Joi", 4),
	FRIDAY("Friday", "Vineri", 5),
	SATURDAY("Saturday", "Sambata", 6),
	SUNDAY("Sunday", "Duminica", 7);


	private String dayStringEng;
	private String dayStringRo;
	private int dayInt;

	WeekDay(String dayStringEng, String dayStringRo, int dayInt) {
		this.dayStringEng = dayStringEng;
		this.dayStringRo = dayStringRo;
		this.dayInt = dayInt;
	}

	public static WeekDay getByString(String dayString) {
		for (WeekDay day : WeekDay.values()) {
			if (day.getDayStringEng().equals(dayString) ||
					day.getDayStringRo().equals(dayString)) {
				return day;
			}
		}

		return null;
	}

	@Override
	@NonNull
	public String toString() {
		return dayStringEng;
	}

	public static WeekDay getByInt(int dayInt) {
		for (WeekDay day : WeekDay.values()) {
			if (day.getDayInt() == dayInt) {
				return day;
			}
		}

		return null;
	}

	public String getDayStringEng() {
		return dayStringEng;
	}

	public String getDayStringRo() {
		return dayStringRo;
	}

	public int getDayInt() {
		return dayInt;
	}
}
