package com.lonn.studentassistant.activities.implementations.student;

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

class StudentActivityFirebaseDispatcher extends Dispatcher {
	private static final Logger LOGGER = Logger.ofClass(StudentActivityFirebaseDispatcher.class);
	private StudentActivityMainLayoutBinding binding;

	private BindableHashMap<CourseViewModel> courseMap;
	private BindableHashMap<OtherActivityViewModel> otherActivityMap;
	private BindableHashMap<RecurringClassViewModel> recurringClassMap;
	private BindableHashMap<OneTimeClassViewModel> oneTimeClassMap;
	private BindableHashMap<CourseViewModel> optionalCoursesMap;
	private BindableHashMap<OtherActivityViewModel> personalActivitiesMap;

	private List<String> oneTimeClassKeys = new ArrayList<>();
	private List<String> recurringClassKeys = new ArrayList<>();

	StudentActivityFirebaseDispatcher(StudentActivity studentActivity) {
		super(studentActivity);
		this.binding = studentActivity.binding;

		courseMap = new BindableHashMap<>(binding, courses);
		otherActivityMap = new BindableHashMap<>(binding, otherActivities);
		recurringClassMap = new BindableHashMap<>(binding, recurringClasses);
		oneTimeClassMap = new BindableHashMap<>(binding, oneTimeClasses);
		optionalCoursesMap = new BindableHashMap<>(binding, optionalCourses);
		personalActivitiesMap = new BindableHashMap<>(binding, myOtherActivities);
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

	void loadAll(String entityKey) {
		if (entityKey != null) {
			loadStudent(entityKey);
		}

		loadCourses();
		loadOtherActivities();
		loadProfessors();
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
						.getById(recurringClassKey)
						.onComplete(recurringClass -> {
									if (isPersonalScheduleClass(recurringClass, binding.getStudent())) {
										recurringClassMap.put(recurringClass);
									}
									else {
										recurringClassMap.remove(recurringClass);
									}
								},
								error -> activity.logAndShowErrorSnack("An error occurred while loading regular classes.", error, LOGGER));
			}
		}
	}

	private void loadOneTimeClasses() {
		if (binding.getOneTimeClasses() == null) {
			for (String oneTimeClassKey : oneTimeClassKeys) {
				firebaseApi.getOneTimeClassService()
						.getById(oneTimeClassKey)
						.onComplete(oneTimeClass -> {
									if (isPersonalScheduleClass(oneTimeClass, binding.getStudent())) {
										oneTimeClassMap.put(oneTimeClass);
									}
									else {
										oneTimeClassMap.remove(oneTimeClass);
									}
								},
								error -> activity.logAndShowErrorSnack("An error occurred while loading special classes.", error, LOGGER));
			}
		}
	}

	private void loadStudent(String key) {
		firebaseApi.getStudentService()
				.getById(key)
				.onComplete(student -> {
							binding.setStudent(student);
							computeClasses();

							if (student.getImageMetadataKey() != null) {
								loadProfileImage(student.imageMetadataKey);
							}
						},
						error -> activity.logAndShowErrorSnack("Error loading your personal data", error, LOGGER));
	}

	private void loadProfileImage(String profileImageKey) {
		firebaseApi.getFileMetadataService()
				.getById(profileImageKey)
				.onComplete(metadata -> firebaseApi.getFileContentService()
									.getById(metadata.getFileContentKey())
									.onComplete(binding::setProfileImageContent,
											error -> activity.logAndShowErrorSnack(
													"Unable to load the profile image",
													error,
													LOGGER))
						,
						error -> activity.logAndShowErrorSnack(
								"Unable to load the profile image",
								error,
								LOGGER));
	}

	private boolean isPersonalScheduleClass(ScheduleClassViewModel scheduleClass,
											StudentViewModel studentViewModel) {
		if (scheduleClass == null) {
			return false;
		}

		String year = studentViewModel.getCycleSpecialization()
				.getInitials() + studentViewModel.getYear();

		String semiYear = year + studentViewModel.getGroup().charAt(0);
		String group = year + studentViewModel.getGroup();

		return scheduleClass.getGroups().contains(year) ||
				scheduleClass.getGroups().contains(semiYear) ||
				scheduleClass.getGroups().contains(group);
	}

	private boolean isPersonalCourse(CourseViewModel course,
									 StudentViewModel studentViewModel) {
		if (course.getPack() == 0) {
			return course.isForCycleAndYearAndSemester(studentViewModel.cycleSpecialization, studentViewModel.year, 1);
		}

		return studentViewModel.getOptionalCourses().contains(course.getKey());
	}

	private boolean isPersonalActivity(OtherActivityViewModel activity,
									   StudentViewModel studentViewModel) {
		return studentViewModel.getOtherActivities().contains(activity.getKey());
	}
}
