package com.lonn.studentassistant.activities.implementations.administrator;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.DisciplineViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.myOtherActivities;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.optionalCourses;
import static com.lonn.studentassistant.BR.otherActivities;
import static com.lonn.studentassistant.BR.recurringClasses;

class AdministratorActivityFirebaseDispatcher extends Dispatcher {
	private static final Logger LOGGER = Logger.ofClass(AdministratorActivityFirebaseDispatcher.class);
	private StudentActivityMainLayoutBinding binding;

	private BindableHashMap<CourseViewModel> courseMap;
	private BindableHashMap<OtherActivityViewModel> otherActivityMap;
	private BindableHashMap<RecurringClassViewModel> recurringClassMap;
	private BindableHashMap<OneTimeClassViewModel> oneTimeClassMap;
	private BindableHashMap<CourseViewModel> optionalCoursesMap;
	private BindableHashMap<OtherActivityViewModel> personalActivitiesMap;

	private List<String> oneTimeClassKeys = new ArrayList<>();
	private List<String> recurringClassKeys = new ArrayList<>();

	AdministratorActivityFirebaseDispatcher(AdministratorActivity administratorActivity) {
		super(administratorActivity);
		this.binding = administratorActivity.binding;

		courseMap = new BindableHashMap<>(binding, courses);
		otherActivityMap = new BindableHashMap<>(binding, otherActivities);
		recurringClassMap = new BindableHashMap<>(binding, recurringClasses);
		oneTimeClassMap = new BindableHashMap<>(binding, oneTimeClasses);
		optionalCoursesMap = new BindableHashMap<>(binding, optionalCourses);
		personalActivitiesMap = new BindableHashMap<>(binding, myOtherActivities);
	}

	void loadAll(String entityKey) {
		if (entityKey != null) {
			loadStudent(entityKey);
		}

		loadCourses();
		loadOtherActivities();
		loadProfessors();
	}

	private void computeClasses() {
		StudentViewModel student = binding.getStudent();

		oneTimeClassKeys.clear();
		recurringClassKeys.clear();

		if (student != null) {
			for (CourseViewModel course : courseMap.values()) {
				if (isPersonalCourse(course, student)) {
					computeClassesForDiscipline(course);
				}
			}
			for (OtherActivityViewModel otherActivity : otherActivityMap.values()) {
				if (isPersonalActivity(otherActivity, student)) {
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
							if (isPersonalScheduleClass(recurringClass, binding.getStudent())) {
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
							if (isPersonalScheduleClass(oneTimeClass, binding.getStudent())) {
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

	private void loadStudent(String key) {
		firebaseApi.getStudentService()
				.getById(key, true)
				.onSuccess(student -> {
					binding.setStudent(student);
					computeClasses();

					if (student.getImageMetadataKey() != null) {
						loadProfileImage(student.getImageMetadataKey());
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

	private boolean isPersonalScheduleClass(ScheduleClassViewModel scheduleClass,
											StudentViewModel studentViewModel) {
		if (scheduleClass == null) {
			return false;
		}

		String year = studentViewModel.getCycleSpecializationYear().getCycleSpecialization()
				.getInitials() + studentViewModel.getCycleSpecializationYear().getYear();

		String semiYear = year + studentViewModel.getGroup().charAt(0);
		String group = year + studentViewModel.getGroup();

		return scheduleClass.getGroups().contains(year) ||
				scheduleClass.getGroups().contains(semiYear) ||
				scheduleClass.getGroups().contains(group);
	}

	private boolean isPersonalCourse(CourseViewModel course,
									 StudentViewModel studentViewModel) {
		if (course.getPack() == 0) {
			return course.isForCycleAndYearAndSemester(studentViewModel.getCycleSpecializationYear(), 1);
		}

		return studentViewModel.getCourses().contains(course.getKey());
	}

	private boolean isPersonalActivity(OtherActivityViewModel activity,
									   StudentViewModel studentViewModel) {
		return studentViewModel.getOtherActivities().contains(activity.getKey());
	}
}
