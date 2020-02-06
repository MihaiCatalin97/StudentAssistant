package com.lonn.studentassistant.firebaselayer.businessLayer.adapters;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Laboratory;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.LaboratoryViewModel;

public class LaboratoryAdapter extends ViewModelAdapter<Laboratory, LaboratoryViewModel> {
	public LaboratoryViewModel adapt(Laboratory laboratory) {
		return LaboratoryViewModel.builder()
				.courseKey(laboratory.getCourse())
				.description(laboratory.getDescription())
				.gradeKeys(laboratory.getGrades())
				.title(laboratory.getTitle())
				.weekNumber(laboratory.getWeekNumber())
				.build()
				.setFileMetadataKeys(laboratory.getFileMetadataKeys())
				.setKey(laboratory.getKey());
	}

	public Laboratory adapt(LaboratoryViewModel laboratoryViewModel) {
		Laboratory result = new Laboratory()
				.setCourse(laboratoryViewModel.getCourseKey())
				.setDescription(laboratoryViewModel.getDescription())
				.setFileMetadataKeys(laboratoryViewModel.getFileMetadataKeys())
				.setGrades(laboratoryViewModel.getGradeKeys())
				.setTitle(laboratoryViewModel.getTitle())
				.setWeekNumber(laboratoryViewModel.getWeekNumber());

		if (laboratoryViewModel.getKey() != null) {
			result.setKey(laboratoryViewModel.getKey());
		}

		return result;
	}
}
