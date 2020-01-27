package com.lonn.studentassistant.firebaselayer.services.abstractions;

import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.services.FileAssociatedEntityService;
import com.lonn.studentassistant.firebaselayer.services.ProfessorService;
import com.lonn.studentassistant.firebaselayer.services.StudentService;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.DisciplineViewModel;

import java.util.List;

public abstract class DisciplineService<T extends Discipline, V extends DisciplineViewModel<T>> extends FileAssociatedEntityService<T, Exception, V> {
	protected StudentService studentService;
	protected ProfessorService professorService;

	protected DisciplineService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	protected void init() {
		studentService = StudentService.getInstance(firebaseConnection);
		professorService = ProfessorService.getInstance(firebaseConnection);
	}

	public Future<Void, Exception> addProfessor(String professorKey, String courseKey) {
		Future<Void, Exception> result = new Future<>();

		getById(courseKey, false)
				.onSuccess(course -> {
					if (!course.getProfessors().contains(professorKey)) {
						course.getProfessors().add(professorKey);

						save(course)
								.onSuccess(result::complete)
								.onError(result::completeExceptionally);

						professorService
								.addCourse(professorKey, courseKey);
					}
					else {
						result.complete(null);
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addStudent(String studentKey, String disciplineKey) {
		Future<Void, Exception> result = new Future<>();

		getById(disciplineKey, false)
				.onSuccess(discipline -> {
					if (!discipline.getStudents().contains(studentKey)) {
						discipline.getStudents().add(studentKey);

						save(discipline)
								.onSuccess(result::complete)
								.onError(result::completeExceptionally);

						linkToStudent(studentKey, disciplineKey);
					}
					else {
						result.complete(null);
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addStudents(List<StudentViewModel> students, String disciplineKey) {
		Future<Void, Exception> result = new Future<>();

		getById(disciplineKey, false)
				.onSuccess(discipline -> {
					if (students != null) {

						for (StudentViewModel student : students) {
							if (!discipline.getStudents().contains(student.getKey())) {
								discipline.getStudents().add(student.getKey());
								discipline.getPendingStudents().remove(student.getKey());

								linkToStudent(student.getKey(), disciplineKey);
							}
						}

						save(discipline)
								.onSuccess(result::complete)
								.onError(result::completeExceptionally);
					}
					else {
						result.complete(null);
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addRecurringClass(String disciplineKey, String recurringClassKey) {
		Future<Void, Exception> result = new Future<>();

		getById(disciplineKey, false)
				.onSuccess(discipline -> {
					if (discipline == null) {
						result.completeExceptionally(new Exception("No discipline found"));
					}
					else {
						if (!discipline.getRecurringClasses().contains(recurringClassKey)) {
							discipline.getRecurringClasses().add(recurringClassKey);
							save(discipline)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> removeRecurringClass(String disciplineKey, String recurringClassKey) {
		Future<Void, Exception> result = new Future<>();

		getById(disciplineKey, false)
				.onSuccess(discipline -> {
					if (discipline == null) {
						result.completeExceptionally(new Exception("No discipline found"));
					}
					else {
						if (discipline.getRecurringClasses().contains(recurringClassKey)) {
							discipline.getRecurringClasses().remove(recurringClassKey);
							save(discipline)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addOneTimeClass(String disciplineKey, String oneTimeClassKey) {
		Future<Void, Exception> result = new Future<>();

		getById(disciplineKey, false)
				.onSuccess(discipline -> {
					if (discipline == null) {
						result.completeExceptionally(new Exception("No discipline found"));
					}
					else {
						if (!discipline.getOneTimeClasses().contains(oneTimeClassKey)) {
							discipline.getOneTimeClasses().add(oneTimeClassKey);
							save(discipline)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> removeOneTimeClass(String disciplineKey, String oneTimeClassKey) {
		Future<Void, Exception> result = new Future<>();

		getById(disciplineKey, false)
				.onSuccess(discipline -> {
					if (discipline == null) {
						result.completeExceptionally(new Exception("No discipline found"));
					}
					else {
						if (discipline.getOneTimeClasses().contains(oneTimeClassKey)) {
							discipline.getOneTimeClasses().remove(oneTimeClassKey);
							save(discipline)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addProfessors(List<ProfessorViewModel> professors, String disciplineKey) {
		Future<Void, Exception> result = new Future<>();

		getById(disciplineKey, false)
				.onSuccess(discipline -> {
					if (professors != null) {

						for (ProfessorViewModel professor : professors) {
							if (!discipline.getProfessors().contains(professor.getKey())) {
								discipline.getProfessors().add(professor.getKey());

								linkToProfessor(professor.getKey(), disciplineKey);
							}
						}

						save(discipline)
								.onSuccess(result::complete)
								.onError(result::completeExceptionally);
					}
					else {
						result.complete(null);
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> removeStudent(V discipline, StudentViewModel student) {
		Future<Void, Exception> result = new Future<>();

		discipline.getStudents().remove(student.getKey());

		save(discipline).onError(result::completeExceptionally)
				.onSuccess(result::complete);

		unlinkToStudent(student, discipline.getKey());

		return result;
	}

	public Future<Void, Exception> removeProfessor(V discipline, ProfessorViewModel professor) {
		Future<Void, Exception> result = new Future<>();

		discipline.getProfessors().remove(professor.getKey());

		save(discipline).onError(result::completeExceptionally)
				.onSuccess(result::complete);

		unlinkToProfessor(professor, discipline.getKey());

		return result;
	}

	protected abstract void linkToStudent(String studentKey, String disciplineKey);

	protected abstract void linkToProfessor(String professorKey, String disciplineKey);

	protected abstract void unlinkToStudent(StudentViewModel student, String disciplineKey);

	protected abstract void unlinkToProfessor(ProfessorViewModel professor, String disciplineKey);
}
