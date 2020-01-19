package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;

import static com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel.builder;

public class ProfessorAdapter extends ViewModelAdapter<Professor, ProfessorViewModel> {
	public ProfessorViewModel adapt(Professor professor) {
		return builder().firstName(professor.getFirstName())
				.lastName(professor.getLastName())
				.cabinet(professor.getCabinet())
				.email(professor.getEmail())
				.phoneNumber(professor.getPhoneNumber())
				.imageMetadataKey(professor.getProfessorImageMetadataKey())
				.rank(professor.getLevel())
				.website(professor.getWebsite())
				.courses(professor.getCourses())
				.otherActivities(professor.getOtherActivities())
				.recurringClasses(professor.getRecurringClasses())
				.oneTimeClasses(professor.getOneTimeClasses())
				.build()
				.setFileMetadataKeys(professor.getFilesMetadata())
				.setKey(professor.getKey());
	}

	public Professor adapt(ProfessorViewModel professorViewModel) {
		Professor result = new Professor().setFirstName(professorViewModel.getFirstName())
				.setLastName(professorViewModel.getLastName())
				.setCabinet(professorViewModel.getCabinet())
				.setEmail(professorViewModel.getEmail())
				.setPhoneNumber(professorViewModel.getPhoneNumber())
				.setProfessorImageMetadataKey(professorViewModel.getImageMetadataKey())
				.setLevel(professorViewModel.getRank())
				.setWebsite(professorViewModel.getWebsite())
				.setCourses(professorViewModel.getCourses())
				.setOtherActivities(professorViewModel.getOtherActivities())
				.setRecurringClasses(professorViewModel.getRecurringClasses())
				.setFilesMetadata(professorViewModel.getFileMetadataKeys())
				.setOneTimeClasses(professorViewModel.getOneTimeClasses());

		if (professorViewModel.getKey() != null) {
			result.setKey(professorViewModel.getKey());
		}

		return result;
	}
}
