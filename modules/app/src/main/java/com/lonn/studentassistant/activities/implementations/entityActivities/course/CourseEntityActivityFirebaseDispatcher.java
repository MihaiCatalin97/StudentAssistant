package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.logging.Logger;

import static com.lonn.studentassistant.BR.files;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.BR.recurringClasses;

class CourseEntityActivityFirebaseDispatcher extends Dispatcher {
	private static final Logger LOGGER = Logger.ofClass(CourseEntityActivityFirebaseDispatcher.class);
	private CourseEntityActivityLayoutBinding binding;
	private BindableHashMap<ProfessorViewModel> professorMap;
	private BindableHashMap<RecurringClassViewModel> recurringClassesMap;
	private BindableHashMap<OneTimeClassViewModel> oneTimeClassesMap;
	private BindableHashMap<FileMetadataViewModel> filesMap;

	CourseEntityActivityFirebaseDispatcher(CourseEntityActivity entityActivity) {
		super(entityActivity);
		this.binding = entityActivity.binding;

		professorMap = new BindableHashMap<>(binding, professors);
		recurringClassesMap = new BindableHashMap<>(binding, recurringClasses);
		oneTimeClassesMap = new BindableHashMap<>(binding, oneTimeClasses);
		filesMap = new BindableHashMap<>(binding, files);
	}

	void loadAll(String courseKey) {
		firebaseApi.getCourseService()
				.getById(courseKey)
				.onComplete(course -> {
							binding.setCourse(course);

							removeNonExistingEntities(professorMap, course.getProfessors());
							removeNonExistingEntities(recurringClassesMap, course.getRecurringClasses());
							removeNonExistingEntities(oneTimeClassesMap, course.getOneTimeClasses());
							removeNonExistingEntities(filesMap, course.getFilesMetadata());

							for (String professorId : course.getProfessors()) {
								firebaseApi.getProfessorService()
										.getById(professorId)
										.onComplete(professorMap::put);
							}

							for (String recurringClassId : course.getRecurringClasses()) {
								firebaseApi.getRecurringClassService()
										.getById(recurringClassId)
										.onComplete(recurringClassesMap::put);
							}

							for (String oneTimeClassId : course.getOneTimeClasses()) {
								firebaseApi.getOneTimeClassService()
										.getById(oneTimeClassId)
										.onComplete(oneTimeClassesMap::put);
							}

							for (String fileId : course.getFilesMetadata()) {
								firebaseApi.getFileMetadataService()
										.getById(fileId)
										.onComplete(filesMap::put);
							}

						},
						error -> activity.logAndShowError("An error occurred while loading the course.",
								new Exception("Loading course: " + error.getMessage()),
								LOGGER));
	}
}
