package com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.DisciplineViewModel;

public abstract class DisciplineAdapter<T extends Discipline, U extends DisciplineViewModel<T>> extends ViewModelAdapter<T, U> {
	protected U adapt(U disciplineViewModel, T discipline) {
		disciplineViewModel.setName(discipline.getDisciplineName())
				.setDescription(discipline.getDescription())
				.setWebsite(discipline.getWebsite())
				.setSemester(discipline.getSemester())
				.setProfessors(discipline.getProfessors())
				.setRecurringClasses(discipline.getRecurringClasses())
				.setOneTimeClasses(discipline.getOneTimeClasses())
				.setFileMetadataKeys(discipline.getFileMetadataKeys())
				.setStudents(discipline.getStudents())
				.setPendingStudents(discipline.getPendingStudents())
				.setKey(discipline.getKey());

		return disciplineViewModel;
	}

	public T adapt(T discipline, U disciplineViewModel) {
		discipline.setDisciplineName(disciplineViewModel.getName())
				.setDescription(disciplineViewModel.getDescription())
				.setWebsite(disciplineViewModel.getWebsite())
				.setSemester(disciplineViewModel.getSemester())
				.setProfessors(disciplineViewModel.getProfessors())
				.setRecurringClasses(disciplineViewModel.getRecurringClasses())
				.setOneTimeClasses(disciplineViewModel.getOneTimeClasses())
				.setStudents(disciplineViewModel.getStudents())
				.setFileMetadataKeys(disciplineViewModel.getFileMetadataKeys())
				.setPendingStudents(disciplineViewModel.getPendingStudents());

		if (disciplineViewModel.getKey() != null) {
			discipline.setKey(disciplineViewModel.getKey());
		}

		return discipline;
	}
}