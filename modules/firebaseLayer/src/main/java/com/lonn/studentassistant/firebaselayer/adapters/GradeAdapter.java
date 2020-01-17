package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.entities.Grade;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;

public class GradeAdapter extends ViewModelAdapter<Grade, GradeViewModel> {
	public GradeViewModel adapt(Grade grade) {
		return GradeViewModel.builder()
				.grade(grade.getValue())
				.laboratoryKey(grade.getLaboratoryKey())
				.studentKey(grade.getStudentKey())
				.build()
				.setKey(grade.getKey());
	}

	public Grade adapt(GradeViewModel grade) {
		Grade result = new Grade()
				.setLaboratoryKey(grade.getLaboratoryKey())
				.setStudentKey(grade.getStudentKey())
				.setValue(grade.getGrade());

		if (grade.getKey() != null) {
			result.setKey(grade.getKey());
		}

		return result;
	}
}
