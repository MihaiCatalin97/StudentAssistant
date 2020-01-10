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

							for (String activityId : professor.getOtherActivities()) {
								firebaseApi.getOtherActivityService()
										.getById(activityId)
										.onComplete(otherActivityMap::put);
							}

							for (String courseId : professor.getCourses()) {
								firebaseApi.getCourseService()
										.getById(courseId)
										.onComplete(courseMap::put);
							}

							for (String recurringClassId : professor.getRecurringClasses()) {
								firebaseApi.getRecurringClassService()
										.getById(recurringClassId)
										.onComplete(recurringClassesMap::put);
							}

							for (String oneTimeClassId : professor.getOneTimeClasses()) {
								firebaseApi.getOneTimeClassService()
										.getById(oneTimeClassId)
										.onComplete(oneTimeClassesMap::put);
							}

							for (String fileId : professor.getFilesMetadata()) {
								firebaseApi.getFileMetadataService()
										.getById(fileId)
										.onComplete(fileMap::put);
							}

						},
						error -> activity.logAndShowError("An error occurred while loading the professor.",
								new Exception("Loading professor: " + error.getMessage()),
								LOGGER)
				);
	}
}
