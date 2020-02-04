package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.entities.Grade;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;

import java.util.Date;

public class GradeAdapter extends ViewModelAdapter<Grade, GradeViewModel> {
	public GradeViewModel adapt(Grade grade) {
		return GradeViewModel.builder()
				.grade(grade.getValue())
				.laboratoryKey(grade.getLaboratoryKey())
				.studentId(grade.getStudentId())
				.studentKey(grade.getStudentKey())
				.date(new Date(grade.getDate()))
				.courseKey(grade.getCourseKey())
				.gradeType(grade.getGradeType())
				.laboratoryNumber(grade.getLaboratoryNumber())
				.build()
				.setKey(grade.getKey());
	}

	public Grade adapt(GradeViewModel grade) {
		Grade result = new Grade()
				.setLaboratoryKey(grade.getLaboratoryKey())
				.setStudentKey(grade.getStudentKey())
				.setStudentId(grade.getStudentId())
				.setValue(grade.getGrade())
				.setDate(grade.getDate().getTime())
				.setGradeType(grade.getGradeType())
				.setLaboratoryNumber(grade.getLaboratoryNumber())
				.setCourseKey(grade.getCourseKey());

		if (grade.getKey() != null) {
			result.setKey(grade.getKey());
		}

		return result;
	}
}
