package com.lonn.studentassistant.activities.implementations.entityActivities.professor;

import com.lonn.studentassistant.activities.abstractions.EntityActivityDispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.services.ProfessorService;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.List;

import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.files;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.otherActivities;
import static com.lonn.studentassistant.BR.recurringClasses;

class ProfessorEntityActivityFirebaseDispatcher extends EntityActivityDispatcher<ProfessorViewModel> {
	private static final Logger LOGGER = Logger.ofClass(ProfessorEntityActivityFirebaseDispatcher.class);
	private BindableHashMap<CourseViewModel> courseMap;
	private BindableHashMap<OtherActivityViewModel> otherActivityMap;
	private BindableHashMap<RecurringClassViewModel> recurringClassesMap;
	private BindableHashMap<OneTimeClassViewModel> oneTimeClassesMap;
	private BindableHashMap<FileMetadataViewModel> fileMap;
	private ProfessorEntityActivity entityActivity;

	ProfessorEntityActivityFirebaseDispatcher(ProfessorEntityActivity entityActivity) {
		super(entityActivity);
		this.entityActivity = entityActivity;

		courseMap = new BindableHashMap<>(entityActivity.getBinding(), courses);
		otherActivityMap = new BindableHashMap<>(entityActivity.getBinding(), otherActivities);
		recurringClassesMap = new BindableHashMap<>(entityActivity.getBinding(), recurringClasses);
		oneTimeClassesMap = new BindableHashMap<>(entityActivity.getBinding(), oneTimeClasses);
		fileMap = new BindableHashMap<>(entityActivity.getBinding(), files);
	}

	void loadAll(String entityKey) {
		firebaseApi.getAuthenticationService()
				.setOnLoggedPersonChange(person -> entityActivity.updateBindingVariables());

		firebaseApi.getProfessorService()
				.getById(entityKey, true)
				.onSuccess(professor -> {
					entityActivity.setActivityEntity(professor);

					removeNonExistingEntities(otherActivityMap, professor.getOtherActivities());
					removeNonExistingEntities(courseMap, professor.getFileMetadataKeys());
					removeNonExistingEntities(recurringClassesMap, professor.getRecurringClasses());
					removeNonExistingEntities(oneTimeClassesMap, professor.getOneTimeClasses());
					removeNonExistingEntities(fileMap, professor.getFileMetadataKeys());

					loadOtherActivities(professor.getOtherActivities());
					loadCourses(professor.getCourses());
					loadRecurringClasses(professor.getRecurringClasses());
					loadOneTimeClasses(professor.getOneTimeClasses());
					loadFiles(professor.getFileMetadataKeys());
					loadImage(professor.getImageMetadataKey());

					entityActivity.updateBindingVariables();
				})
				.onError(error -> activity.logAndShowErrorSnack("An error occurred while loading the professor.",
						new Exception("Loading professor: " + error.getMessage()),
						LOGGER)
				);
	}

	private void loadFiles(List<String> fileIds) {
		for (String fileId : fileIds) {
			firebaseApi.getFileMetadataService()
					.getById(fileId, true)
					.onSuccess(fileMap::put);
		}
	}

	private void loadCourses(List<String> courseIds) {
		for (String courseId : courseIds) {
			firebaseApi.getCourseService()
					.getById(courseId, true)
					.onSuccess(courseMap::put);
		}
	}

	private void loadOtherActivities(List<String> activityIds) {
		for (String activityId : activityIds) {
			firebaseApi.getOtherActivityService()
					.getById(activityId, true)
					.onSuccess(otherActivityMap::put);
		}
	}

	private void loadRecurringClasses(List<String> recurringClassIds) {
		for (String recurringClassId : recurringClassIds) {
			firebaseApi.getRecurringClassService()
					.getById(recurringClassId, true)
					.onSuccess(recurringClassesMap::put);
		}
	}

	private void loadOneTimeClasses(List<String> oneTimeClassIds) {
		for (String oneTimeClassId : oneTimeClassIds) {
			firebaseApi.getOneTimeClassService()
					.getById(oneTimeClassId, true)
					.onSuccess(oneTimeClassesMap::put);
		}
	}

	private void loadImage(String imageMetadataId) {
		if (imageMetadataId != null) {
			firebaseApi.getFileMetadataService()
					.getById(imageMetadataId, true)
					.onSuccess(metadata ->
							firebaseApi.getFileContentService()
									.getById(metadata.getFileContentKey(), true)
									.onSuccess(entityActivity.getBinding()::setProfessorImageContent)
									.onError(error -> activity.logAndShowErrorSnack(
											"Unable to load the professors image",
											error,
											LOGGER)))
					.onError(error -> activity.logAndShowErrorSnack(
							"Unable to load the professors image",
							error,
							LOGGER));
		}
	}

	@Override
	public ProfessorService getService() {
		return firebaseApi.getProfessorService();
	}

	@Override
	public String getEntityName() {
		return "professor";
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}
}
