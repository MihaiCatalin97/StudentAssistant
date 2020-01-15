package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.entities.Laboratory;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;

public class LaboratoryAdapter extends ViewModelAdapter<Laboratory, LaboratoryViewModel> {
	public LaboratoryViewModel adapt(Laboratory laboratory) {
		return LaboratoryViewModel.builder()
				.courseKey(laboratory.getCourse())
				.description(laboratory.getDescription())
				.fileMetadataKeys(laboratory.getFilesMetadata())
				.gradeKeys(laboratory.getGrades())
				.title(laboratory.getTitle())
				.weekNumber(laboratory.getWeekNumber())
				.build()
				.setKey(laboratory.getKey());
	}

	public Laboratory adapt(LaboratoryViewModel laboratoryViewModel) {
		Laboratory result = new Laboratory()
				.setCourse(laboratoryViewModel.getCourseKey())
				.setDescription(laboratoryViewModel.getDescription())
				.setFilesMetadata(laboratoryViewModel.getFileMetadataKeys())
				.setGrades(laboratoryViewModel.getGradeKeys())
				.setTitle(laboratoryViewModel.getTitle())
				.setWeekNumber(laboratoryViewModel.getWeekNumber());

		if (laboratoryViewModel.getKey() != null) {
			result.setKey(laboratoryViewModel.getKey());
		}

		return result;
	}
}
