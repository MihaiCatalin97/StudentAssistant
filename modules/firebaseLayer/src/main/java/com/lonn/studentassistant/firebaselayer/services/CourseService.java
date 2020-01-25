package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.CourseAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;

import java.util.List;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;

public class CourseService extends FileAssociatedEntityService<Course, Exception, CourseViewModel> {
	private static CourseService instance;
	private StudentService studentService;
	private ProfessorService professorService;

	private CourseService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public static CourseService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new CourseService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		adapter = new CourseAdapter();
		studentService = StudentService.getInstance(firebaseConnection);
		professorService = ProfessorService.getInstance(firebaseConnection);
	}

	public Future<Void, Exception> linkLaboratoryToCourse(String courseKey, String laboratoryKey) {
		Future<Void, Exception> result = new Future<>();

		getById(courseKey, false)
				.onSuccess(course -> {
					if (!course.getLaboratories().contains(laboratoryKey)) {
						course.getLaboratories().add(laboratoryKey);

						save(course)
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

	public Future<Void, Exception> addStudent(String studentKey, String courseKey) {
		Future<Void, Exception> result = new Future<>();

		getById(courseKey, false)
				.onSuccess(course -> {
					if (!course.getStudents().contains(studentKey)) {
						course.getStudents().add(studentKey);

						save(course)
								.onSuccess(result::complete)
								.onError(result::completeExceptionally);

						studentService
								.addCourse(studentKey, courseKey);
					}
					else {
						result.complete(null);
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addStudents(List<StudentViewModel> students, String courseKey) {
		Future<Void, Exception> result = new Future<>();

		getById(courseKey, false)
				.onSuccess(course -> {
					if (students != null) {

						for (StudentViewModel student : students) {
							if (!course.getStudents().contains(student.getKey())) {
								course.getStudents().add(student.getKey());
								course.getPendingStudents().remove(student.getKey());

								studentService.addCourse(student.getKey(), courseKey);
							}
						}

						save(course)
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


	public Future<Void, Exception> addProfessors(List<ProfessorViewModel> professors, String courseKey) {
		Future<Void, Exception> result = new Future<>();

		getById(courseKey, false)
				.onSuccess(course -> {
					if (professors != null) {

						for (ProfessorViewModel professor : professors) {
							if (!course.getProfessors().contains(professor.getKey())) {
								course.getProfessors().add(professor.getKey());

								professorService.addCourse(professor.getKey(), courseKey);
							}
						}

						save(course)
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

	public Future<Void, Exception> removeStudent(CourseViewModel course, StudentViewModel student) {
		Future<Void, Exception> result = new Future<>();

		course.getStudents().remove(student.getKey());
		student.getCourses().remove(course.getKey());

		save(course).onError(result::completeExceptionally)
				.onSuccess(result::complete);
		studentService.save(student)
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> removeProfessor(CourseViewModel course, ProfessorViewModel professor) {
		Future<Void, Exception> result = new Future<>();

		course.getProfessors().remove(professor.getKey());
		professor.getCourses().remove(course.getKey());

		save(course).onError(result::completeExceptionally)
				.onSuccess(result::complete);

		professorService.save(professor)
				.onError(result::completeExceptionally);

		return result;
	}

	@Override
	protected DatabaseTable<Course> getDatabaseTable() {
		return COURSES;
	}
}
