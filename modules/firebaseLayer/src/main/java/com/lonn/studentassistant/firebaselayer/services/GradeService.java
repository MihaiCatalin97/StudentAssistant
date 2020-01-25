package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.GradeAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Grade;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.GRADES;

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

		studentService.addGradeToStudent(grade)
				.onSuccess(none -> {
					save(grade)
							.onSuccess(none2 -> {
								linkToLaboratory(grade)
										.onSuccess(none3 -> {
													laboratoryService.getById(grade.getLaboratoryKey(), false)
															.onSuccess(laboratory -> {
																courseService.addStudent(grade.getStudentKey(),
																		laboratory.getCourseKey());
																result.complete(null);
															});
												}
										)
										.onError(error -> deleteGradeAndCompleteExceptionally(grade.getKey(),
												result,
												error));
							})
							.onError(result::completeExceptionally);
				})
				.onError(result::completeExceptionally);
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

					laboratoryService.getById(grade.getLaboratoryKey(), false)
							.onSuccess(laboratory -> {
								laboratory.getGradeKeys().remove(entityId);
								laboratoryService.save(laboratory);
							});

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
}
