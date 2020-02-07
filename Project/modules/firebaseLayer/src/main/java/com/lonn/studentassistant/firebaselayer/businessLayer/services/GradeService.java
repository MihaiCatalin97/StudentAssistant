package com.lonn.studentassistant.firebaselayer.businessLayer.services;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.GradeAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.Future;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Grade;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.GradeViewModel;

import java.util.List;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTableContainer.GRADES;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.LABORATORY;

public class GradeService extends Service<Grade, Exception, GradeViewModel> {
	private static GradeService instance;
	private LaboratoryService laboratoryService;
	private StudentService studentService;
	private CourseService courseService;

	private GradeService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public static GradeService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new GradeService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		super.init();
		adapter = new GradeAdapter();
		studentService = StudentService.getInstance(firebaseConnection);
		laboratoryService = LaboratoryService.getInstance(firebaseConnection);
		courseService = CourseService.getInstance(firebaseConnection);
	}

	@Override
	public DatabaseTable<Grade> getDatabaseTable() {
		return GRADES;
	}

	public Future<Void, Exception> saveAndLink(GradeViewModel grade) {
		Future<Void, Exception> result = new Future<>();

		studentHasGradeType(grade)
				.onSuccess(hasGrade -> {
					if (hasGrade) {
						result.completeExceptionally(new Exception("Student already has a grade for this " + grade.getGradeType().toString().toLowerCase()));
						return;
					}

					studentService.addGradeToStudent(grade)
							.onSuccess(none -> save(grade)
									.onSuccess(none2 -> {
										linkGrade(grade).onSuccess(result::complete)
												.onError(result::completeExceptionally);
									})
									.onError(result::completeExceptionally))
							.onError(result::completeExceptionally);
				})
				.onError(result::completeExceptionally);

		return result;
	}

	private Future<Void, Exception> linkGrade(GradeViewModel grade) {
		Future<Void, Exception> result = new Future<>();

		if (grade.getLaboratoryKey() != null) {
			linkToLaboratory(grade).onSuccess(none3 ->
					laboratoryService.getById(grade.getLaboratoryKey(), false)
							.onSuccess(laboratory -> {
								courseService.addStudent(grade.getStudentKey(),
										laboratory.getCourseKey());
								result.complete(null);
							})
			)
					.onError(error -> deleteGradeAndCompleteExceptionally(grade.getKey(),
							result,
							error));
		}
		if (grade.getCourseKey() != null) {
			linkToCourse(grade).onSuccess(none3 ->
					courseService.addStudent(grade.getStudentKey(),
							grade.getCourseKey())
							.onSuccess(result::complete))
					.onError(error -> deleteGradeAndCompleteExceptionally(grade.getKey(),
							result,
							error));
		}

		return result;
	}

	private Future<Void, Exception> linkToLaboratory(GradeViewModel grade) {
		Future<Void, Exception> result = new Future<>();

		laboratoryService.addGradeToLaboratory(grade.getKey(), grade.getLaboratoryKey())
				.onSuccess(result::complete)
				.onError(error -> deleteGradeAndCompleteExceptionally(grade.getKey(),
						result,
						error));

		return result;
	}

	private Future<Void, Exception> linkToCourse(GradeViewModel grade) {
		Future<Void, Exception> result = new Future<>();

		courseService.addGradeToCourse(grade.getKey(), grade.getCourseKey())
				.onSuccess(result::complete)
				.onError(error -> deleteGradeAndCompleteExceptionally(grade.getKey(),
						result,
						error));

		return result;
	}

	@Override
	public Future<Void, Exception> deleteById(String entityId) {
		Future<Void, Exception> result = new Future<>();

		getById(entityId, false)
				.onSuccess(grade -> {
					studentService.getById(grade.getStudentKey(), false)
							.onSuccess(student -> {
								student.getGradeKeys().remove(entityId);
								studentService.save(student);
							});

					if (grade.getLaboratoryKey() != null) {
						laboratoryService.getById(grade.getLaboratoryKey(), false)
								.onSuccess(laboratory -> {
									laboratory.getGradeKeys().remove(entityId);
									laboratoryService.save(laboratory);
								});
					}
					if (grade.getCourseKey() != null) {
						courseService.getById(grade.getCourseKey(), false)
								.onSuccess(course -> {
									course.getGrades().remove(entityId);
									courseService.save(course);
								});
					}

					super.deleteById(entityId)
							.onSuccess(result::complete)
							.onError(result::completeExceptionally);
				})
				.onError();

		return result;
	}

	private void deleteGradeAndCompleteExceptionally(String gradeKey,
													 Future<Void, Exception> future,
													 Exception exception) {
		deleteById(gradeKey);
		future.completeExceptionally(exception);
	}

	protected PermissionLevel getPermissionLevel(Grade grade) {
		return authenticationService.getPermissionLevel(grade);
	}

	private Future<Boolean, Exception> studentHasGradeType(GradeViewModel grade) {
		Future<Boolean, Exception> result = new Future<>();

		studentService.getGradesForStudentId(grade.getStudentId())
				.onSuccess(gradeKeys -> {
					if (gradeKeys.size() > 0) {
						getByIds(gradeKeys, false)
								.onSuccess(grades -> result.complete(listContainsGradeOfSameType(grades, grade)))
								.onError(result::completeExceptionally);
					}
					else {
						result.complete(false);
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	private boolean listContainsGradeOfSameType(List<GradeViewModel> gradeList, GradeViewModel grade) {
		for (GradeViewModel gradeFromList : gradeList) {
			if (gradesHaveSameType(gradeFromList, grade)) {
				return true;
			}
		}

		return false;
	}

	private boolean gradesHaveSameType(GradeViewModel grade1, GradeViewModel grade2) {
		if (!grade1.getCourseKey().equals(grade2.getCourseKey())) {
			return false;
		}
		if (grade1.getGradeType().equals(grade2.getGradeType())) {
			if (grade1.getGradeType().equals(LABORATORY)) {
				return grade1.getLaboratoryKey().equals(grade2.getLaboratoryKey());
			}
			return true;
		}
		return false;
	}
}
