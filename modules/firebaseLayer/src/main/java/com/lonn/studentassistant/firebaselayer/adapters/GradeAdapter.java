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
				.build()
				.setKey(grade.getKey());
	}

	public Grade adapt(GradeViewModel grade) {
		Grade result = new Grade()
				.setLaboratoryKey(grade.getLaboratoryKey())
				.setStudentKey(grade.getStudentKey())
				.setStudentId(grade.getStudentId())
				.setValue(grade.getGrade())
				.setDate(grade.getDate().getTime());

		if (grade.getKey() != null) {
			result.setKey(grade.getKey());
		}

		return result;
	}
}
