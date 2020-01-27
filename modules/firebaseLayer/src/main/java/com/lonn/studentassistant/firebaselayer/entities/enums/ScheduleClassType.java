package com.lonn.studentassistant.firebaselayer.entities.enums;

import androidx.annotation.NonNull;

import lombok.Getter;

@Getter
public enum ScheduleClassType {
	EXAM("Examen", "Exam"),
	PRACTICAL_EXAM("Practic", "Practical exam"),
	PRACTICAL_TEST_EXAM("Test practic", "Practical exam"),
	PROJECTS_EXAM("Proiecte", "Projects"),
	PARTIAL_EXAM("Partial", "Partial"),
	ARREARS_EXAM("Restante", "Arrears"),
	LABORATORY("Laborator", "Laboratory"),
	SEMINAR("Seminar", "Seminar"),
	COURSE("Curs", "Course"),
	CONSULTATIONS("Consultatii", "Consultations"),
	SCIENTIFIC_CLASS("Cerc stiintific", "Scientific class"),
	OTHER_ACTIVITIES("Alte activitati", "Other activity");

	private String typeRo;
	private String typeEn;

	ScheduleClassType(String typeRo, String typeEn) {
		this.typeEn = typeEn;
		this.typeRo = typeRo;
	}

	public static ScheduleClassType valueOfRoString(String roString) {
		for (ScheduleClassType type : ScheduleClassType.values()) {
			if (type.getTypeRo().equals(roString)) {
				return type;
			}
		}

		return OTHER_ACTIVITIES;
	}

	@NonNull
	@Override
	public String toString() {
		return typeEn;
	}
}
