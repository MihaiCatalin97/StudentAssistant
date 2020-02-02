package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.LaboratoryAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Laboratory;
import com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;

import java.util.List;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.LABORATORIES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.LaboratoryField.COURSE_KEY;

public class LaboratoryService extends FileAssociatedEntityService<Laboratory, Exception, LaboratoryViewModel> {
	private static LaboratoryService instance;
	private StudentService studentService;
	private CourseService courseService;
	private GradeService gradeService;

	private LaboratoryService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public static LaboratoryService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new LaboratoryService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		super.init();
		adapter = new LaboratoryAdapter();
		studentService = StudentService.getInstance(firebaseConnection);
		courseService = CourseService.getInstance(firebaseConnection);
		gradeService = GradeService.getInstance(firebaseConnection);
	}

	public Future<Void, Exception> saveAndLinkLaboratory(LaboratoryViewModel laboratory) {
		Future<Void, Exception> result = new Future<>();

		save(laboratory).onSuccess(none ->
				courseService
						.linkLaboratoryToCourse(laboratory.getCourseKey(), laboratory.getKey())
						.onSuccess(result::complete)
						.onError(error -> {
							deleteById(laboratory.getKey());
							result.completeExceptionally(error);
						}))
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addGradeToLaboratory(String gradeKey, String laboratoryKey) {
		Future<Void, Exception> result = new Future<>();

		getById(laboratoryKey, false)
				.onSuccess(laboratory -> {
					if (laboratory == null) {
						result.completeExceptionally(new Exception("Laboratory not found"));
						return;
					}

					if (!laboratory.getGradeKeys().contains(gradeKey)) {
						laboratory.getGradeKeys().add(gradeKey);

						save(laboratory)
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

	public Future<List<LaboratoryViewModel>, Exception> getByCourseKey(String courseKey, boolean subscribe) {
		Future<List<LaboratoryViewModel>, Exception> result = new Future<>();

		firebaseConnection.execute(new GetRequest<Laboratory, Exception>()
				.databaseTable(LABORATORIES)
				.predicate(where(COURSE_KEY).equalTo(courseKey))
				.subscribe(subscribe)
				.onSuccess(laboratories -> result.complete(transform(laboratories)))
				.onError(result::completeExceptionally));

		return result;
	}

	public Future<LaboratoryViewModel, Exception> getByCourseKeyAndWeek(String courseKey,
																		int week,
																		boolean subscribe) {
		Future<LaboratoryViewModel, Exception> result = new Future<>();

		getByCourseKey(courseKey, subscribe)
				.onSuccess(laboratories -> {
					for (LaboratoryViewModel laboratory : laboratories) {
						if (laboratory.getWeekNumber() == week) {
							result.complete(laboratory);
							return;
						}
					}

					result.complete(null);
				})
				.onError(result::completeExceptionally);

		return result;
	}

	@Override
	public Future<Void, Exception> deleteById(String entityKey) {
		Future<Void, Exception> result = new Future<>();

		getById(entityKey, false)
				.onSuccess(laboratory -> {
					if (laboratory != null) {
						for (String fileKey : laboratory.getFileMetadataKeys()) {
							fileMetadataService.deleteMetadataAndContent(fileKey);
						}

						for (String gradeKey : laboratory.getGradeKeys()) {
							gradeService.deleteById(gradeKey);
						}

						courseService.getById(laboratory.getCourseKey(), false)
								.onSuccess(course -> {
									course.getLaboratories().remove(entityKey);
									courseService.save(course);
								});

						super.deleteById(entityKey)
								.onSuccess(result::complete)
								.onError(result::completeExceptionally);
					}
					else {
						result.completeExceptionally(new Exception("Laboratory not found"));
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	@Override
	protected DatabaseTable<Laboratory> getDatabaseTable() {
		return LABORATORIES;
	}

	protected PermissionLevel getPermissionLevel(Laboratory laboratory) {
		return authenticationService.getPermissionLevel(laboratory);
	}
}