package com.lonn.studentassistant.firebaselayer.adapters.abstractions;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.DisciplineViewModel;

public abstract class DisciplineAdapter<T extends Discipline, U extends DisciplineViewModel<T>> extends ViewModelAdapter<T, U> {
	protected U adapt(U disciplineViewModel, T discipline) {
		disciplineViewModel.setName(discipline.getDisciplineName())
				.setDescription(discipline.getDescription())
				.setWebsite(discipline.getWebsite())
				.setSemester(discipline.getSemester())
				.setProfessors(discipline.getProfessors())
				.setRecurringClasses(discipline.getRecurringClasses())
				.setOneTimeClasses(discipline.getOneTimeClasses())
				.setFilesMetadata(discipline.getFilesMetadata())
				.setStudents(discipline.getStudents())
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
				.setFilesMetadata(disciplineViewModel.getFilesMetadata());

		if (disciplineViewModel.getKey() != null) {
			discipline.setKey(disciplineViewModel.getKey());
		}

		return discipline;
	}
}