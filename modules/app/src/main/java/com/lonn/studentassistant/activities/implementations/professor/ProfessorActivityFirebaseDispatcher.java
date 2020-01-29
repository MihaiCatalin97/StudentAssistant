package com.lonn.studentassistant.activities.implementations.professor;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.databinding.ProfessorActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.DisciplineViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.myOtherActivities;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.optionalCourses;
import static com.lonn.studentassistant.BR.otherActivities;
import static com.lonn.studentassistant.BR.personalCourses;
import static com.lonn.studentassistant.BR.personalOtherActivities;
import static com.lonn.studentassistant.BR.recurringClasses;

class ProfessorActivityFirebaseDispatcher extends Dispatcher {
	private static final Logger LOGGER = Logger.ofClass(ProfessorActivityFirebaseDispatcher.class);
	private ProfessorActivityMainLayoutBinding binding;

	private BindableHashMap<CourseViewModel> courseMap;
	private BindableHashMap<OtherActivityViewModel> otherActivityMap;
	private BindableHashMap<RecurringClassViewModel> recurringClassMap;
	private BindableHashMap<OneTimeClassViewModel> oneTimeClassMap;
	private BindableHashMap<CourseViewModel> personalCoursesMap;
	private BindableHashMap<OtherActivityViewModel> personalActivitiesMap;

	private List<String> oneTimeClassKeys = new ArrayList<>();
	private List<String> recurringClassKeys = new ArrayList<>();

	ProfessorActivityFirebaseDispatcher(ProfessorActivity professorActivity) {
		super(professorActivity);
		this.binding = professorActivity.binding;

		courseMap = new BindableHashMap<>(binding, courses);
		otherActivityMap = new BindableHashMap<>(binding, otherActivities);
		recurringClassMap = new BindableHashMap<>(binding, recurringClasses);
		oneTimeClassMap = new BindableHashMap<>(binding, oneTimeClasses);
		personalCoursesMap = new BindableHashMap<>(binding, personalCourses);
		personalActivitiesMap = new BindableHashMap<>(binding, personalOtherActivities);
	}

	void loadAll(String entityKey) {
		if (entityKey != null) {
			loadProfessor(entityKey);
		}

		loadCourses();
		loadOtherActivities();
		loadProfessors();
	}

	private void computeClasses() {
		ProfessorViewModel professor = binding.getProfessor();

		oneTimeClassKeys.clear();
		recurringClassKeys.clear();

		if (professor != null) {
			for (CourseViewModel course : courseMap.values()) {
				if (professor.getCourses().contains(course.getKey())) {
					computeClassesForDiscipline(course);
				}
			}
			for (OtherActivityViewModel otherActivity : otherActivityMap.values()) {
				if (professor.getOtherActivities().contains(otherActivity.getKey())) {
					computeClassesForDiscipline(otherActivity);
				}
			}
		}

		loadRecurringClasses();
		loadOneTimeClasses();
	}

	private void computeClassesForDiscipline(DisciplineViewModel<?> discipline) {
		oneTimeClassKeys.addAll(discipline.getOneTimeClasses());
		recurringClassKeys.addAll(discipline.getRecurringClasses());
	}

	private void loadCourses() {
		if (binding.getCourses() == null) {
			firebaseApi.getCourseService()
					.getAll()
					.onComplete(receivedCourses -> {
								courseMap = new BindableHashMap<>(binding, courses, receivedCourses);
								computeClasses();
							},
							error -> activity.logAndShowErrorSnack("An error occurred while loading activities.", error, LOGGER));
		}
	}

	private void loadOtherActivities() {
		if (binding.getOtherActivities() == null) {
			firebaseApi.getOtherActivityService()
					.getAll()
					.onComplete(receivedOtherActivities -> {
								otherActivityMap = new BindableHashMap<>(binding, otherActivities, receivedOtherActivities);
								computeClasses();
							},
							error -> activity.logAndShowErrorSnack("An error occurred while loading activities.", error, LOGGER));
		}
	}

	private void loadProfessors() {
		if (binding.getProfessors() == null) {
			firebaseApi.getProfessorService()
					.getAll()
					.onComplete(binding::setProfessors,
							error -> activity.logAndShowErrorSnack("An error occurred while loading professors.", error, LOGGER));
		}
	}

	private void loadRecurringClasses() {
		if (binding.getRecurringClasses() == null) {
			for (String recurringClassKey : recurringClassKeys) {
				firebaseApi.getRecurringClassService()
						.getById(recurringClassKey, true)
						.onSuccess(recurringClass -> {
							if (binding.getProfessor().getRecurringClasses().contains(recurringClass.getKey())) {
								recurringClassMap.put(recurringClass);
							}
							else {
								recurringClassMap.remove(recurringClass);
							}
						})
						.onError(error -> activity.logAndShowErrorSnack("An error occurred while loading regular classes.", error, LOGGER));
			}
		}
	}

	private void loadOneTimeClasses() {
		if (binding.getOneTimeClasses() == null) {
			for (String oneTimeClassKey : oneTimeClassKeys) {
				firebaseApi.getOneTimeClassService()
						.getById(oneTimeClassKey, true)
						.onSuccess(oneTimeClass -> {
							if (binding.getProfessor().getOneTimeClasses().contains(oneTimeClass.getKey())) {
								oneTimeClassMap.put(oneTimeClass);
							}
							else {
								oneTimeClassMap.remove(oneTimeClass);
							}
						})
						.onError(error -> activity.logAndShowErrorSnack("An error occurred while loading special classes.", error, LOGGER));
			}
		}
	}

	private void loadProfessor(String key) {
		firebaseApi.getProfessorService()
				.getById(key, true)
				.onSuccess(professor -> {
					binding.setProfessor(professor);
					computeClasses();

					if (professor.getImageMetadataKey() != null) {
						loadProfileImage(professor.getImageMetadataKey());
					}
				})
				.onError(error -> activity.logAndShowErrorSnack("Error loading your personal data", error, LOGGER));
	}

	private void loadProfileImage(String profileImageKey) {
		firebaseApi.getFileMetadataService()
				.getById(profileImageKey, true)
				.onSuccess(metadata -> firebaseApi.getFileContentService()
						.getById(metadata.getFileContentKey(), true)
						.onSuccess(binding::setProfileImageContent)
						.onError(error -> activity.logAndShowErrorSnack(
								"Unable to load the profile image",
								error,
								LOGGER)))
				.onError(error -> activity.logAndShowErrorSnack(
						"Unable to load the profile image",
						error,
						LOGGER));
	}
}
