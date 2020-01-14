package com.lonn.studentassistant.activities.implementations.entityActivities.professor;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.List;

import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.files;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.otherActivities;
import static com.lonn.studentassistant.BR.recurringClasses;

class ProfessorEntityActivityFirebaseDispatcher extends Dispatcher {
	private static final Logger LOGGER = Logger.ofClass(ProfessorEntityActivityFirebaseDispatcher.class);
	private ProfessorEntityActivityLayoutBinding binding;
	private BindableHashMap<CourseViewModel> courseMap;
	private BindableHashMap<OtherActivityViewModel> otherActivityMap;
	private BindableHashMap<RecurringClassViewModel> recurringClassesMap;
	private BindableHashMap<OneTimeClassViewModel> oneTimeClassesMap;
	private BindableHashMap<FileMetadataViewModel> fileMap;

	ProfessorEntityActivityFirebaseDispatcher(ProfessorEntityActivity entityActivity) {
		super(entityActivity);
		this.binding = entityActivity.binding;

		courseMap = new BindableHashMap<>(binding, courses);
		otherActivityMap = new BindableHashMap<>(binding, otherActivities);
		recurringClassesMap = new BindableHashMap<>(binding, recurringClasses);
		oneTimeClassesMap = new BindableHashMap<>(binding, oneTimeClasses);
		fileMap = new BindableHashMap<>(binding, files);
	}

	void loadAll(String entityKey) {
		firebaseApi.getProfessorService()
				.getById(entityKey)
				.onComplete(professor -> {
							binding.setProfessor(professor);

							removeNonExistingEntities(otherActivityMap, professor.getOtherActivities());
							removeNonExistingEntities(courseMap, professor.getFilesMetadata());
							removeNonExistingEntities(recurringClassesMap, professor.getRecurringClasses());
							removeNonExistingEntities(oneTimeClassesMap, professor.getOneTimeClasses());
							removeNonExistingEntities(fileMap, professor.getFilesMetadata());

							loadOtherActivities(professor.getOtherActivities());
							loadCourses(professor.getCourses());
							loadRecurringClasses(professor.getRecurringClasses());
							loadOneTimeClasses(professor.getOneTimeClasses());
							loadFiles(professor.getFilesMetadata());
							loadImage(professor.professorImageMetadataKey);
						},
						error -> activity.logAndShowError("An error occurred while loading the professor.",
								new Exception("Loading professor: " + error.getMessage()),
								LOGGER)
				);
	}

	private void loadFiles(List<String> fileIds) {
		for (String fileId : fileIds) {
			firebaseApi.getFileMetadataService()
					.getById(fileId)
					.onComplete(fileMap::put);
		}
	}

	private void loadCourses(List<String> courseIds) {
		for (String courseId : courseIds) {
			firebaseApi.getCourseService()
					.getById(courseId)
					.onComplete(courseMap::put);
		}
	}

	private void loadOtherActivities(List<String> activityIds) {
		for (String activityId : activityIds) {
			firebaseApi.getOtherActivityService()
					.getById(activityId)
					.onComplete(otherActivityMap::put);
		}
	}

	private void loadRecurringClasses(List<String> recurringClassIds) {
		for (String recurringClassId : recurringClassIds) {
			firebaseApi.getRecurringClassService()
					.getById(recurringClassId)
					.onComplete(recurringClassesMap::put);
		}
	}

	private void loadOneTimeClasses(List<String> oneTimeClassIds) {
		for (String oneTimeClassId : oneTimeClassIds) {
			firebaseApi.getOneTimeClassService()
					.getById(oneTimeClassId)
					.onComplete(oneTimeClassesMap::put);
		}
	}

	private void loadImage(String imageMetadataId) {
		if (imageMetadataId != null) {
			firebaseApi.getFileMetadataService()
					.getById(imageMetadataId)
					.onComplete(metadata ->
									firebaseApi.getFileContentService()
											.getById(metadata.getFileContentKey())
											.onComplete(binding::setProfessorImageContent,
													error -> activity.logAndShowError(
															"Unable to load the professors image",
															error,
															LOGGER))
							,
							error -> activity.logAndShowError(
									"Unable to load the professors image",
									error,
									LOGGER));
		}
	}
}
