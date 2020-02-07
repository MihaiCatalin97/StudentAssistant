package com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums;

import lombok.Getter;

public enum GradeType {
	LABORATORY("Laboratory"),
	PARTIAL_EXAM("Partial exam"),
	EXAM("Exam"),
	PROJECT("Project"),
	PARTIAL_ARREARS("Partial arrears"),
	EXAM_ARREARS("Exam arrears"),
	PROJECT_ARREARS("Project arrears");

	@Getter
	private String gradeType;

	GradeType(String gradeType) {
		this.gradeType = gradeType;
	}
}
