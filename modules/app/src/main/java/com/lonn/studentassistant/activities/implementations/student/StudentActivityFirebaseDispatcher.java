package com.lonn.studentassistant.activities.implementations.student;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.DisciplineViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.otherActivities;
import static com.lonn.studentassistant.BR.personalActivities;
import static com.lonn.studentassistant.BR.personalCourses;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.BR.recurringClasses;

class StudentActivityFirebaseDispatcher extends Dispatcher<StudentActivity, StudentViewModel> {
	private static final Logger LOGGER = Logger.ofClass(StudentActivityFirebaseDispatcher.class);
	private StudentActivityMainLayoutBinding binding;

	private BindableHashMap<CourseViewModel> courseMap;
	private BindableHashMap<OtherActivityViewModel> otherActivityMap;
	private BindableHashMap<ProfessorViewModel> professorsMap;

	private BindableHashMap<RecurringClassViewModel> recurringClassMap;
	private BindableHashMap<OneTimeClassViewModel> oneTimeClassMap;
	private BindableHashMap<CourseViewModel> personalCoursesMap;
	private BindableHashMap<OtherActivityViewModel> personalActivitiesMap;

	private List<String> oneTimeClassKeys = new ArrayList<>();
	private List<String> recurringClassKeys = new ArrayList<>();

	StudentActivityFirebaseDispatcher(StudentActivity studentActivity) {
		super(studentActivity);
		this.binding = studentActivity.binding;

		courseMap = new BindableHashMap<>(binding, courses);
		otherActivityMap = new BindableHashMap<>(binding, otherActivities);
		professorsMap = new BindableHashMap<>(binding, com.lonn.studentassistant.BR.professors);

		recurringClassMap = new BindableHashMap<>(binding, recurringClasses);
		oneTimeClassMap = new BindableHashMap<>(binding, oneTimeClasses);
		personalCoursesMap = new BindableHashMap<>(binding, personalCourses);
		personalActivitiesMap = new BindableHashMap<>(binding, personalActivities);
	}

	public void loadAll(String entityKey) {
		if (entityKey != null) {
			loadStudent(entityKey);
		}

		loadCourses();
		loadOtherActivities();
		loadProfessors();
	}

	private void computePersonalCourses() {
		if (currentProfile != null) {
			for (CourseViewModel personalCourse : personalCoursesMap.values()) {
				if (!currentProfile.getCourses().contains(personalCourse.getKey())) {
					personalCoursesMap.remove(personalCourse);
				}
			}
			for (String courseKey : currentProfile.getCourses()) {
				personalCoursesMap.put(courseMap.get(courseKey));
			}

			computeClasses();
		}
	}

	private void computePersonalActivities() {
		if (currentProfile != null) {
			for (OtherActivityViewModel personalActivity : personalActivitiesMap.values()) {
				if (!currentProfile.getOtherActivities().contains(personalActivity.getKey())) {
					personalActivitiesMap.remove(personalActivity);
				}
			}
			for (String activityKey : currentProfile.getOtherActivities()) {
				personalActivitiesMap.put(otherActivityMap.get(activityKey));
			}

			computeClasses();
		}
	}

	private void computeClasses() {
		StudentViewModel student = binding.getStudent();

		oneTimeClassKeys.clear();
		recurringClassKeys.clear();

		if (student != null) {
			for (CourseViewModel course : personalCoursesMap.values()) {
				computeClassesForDiscipline(course);
			}
			for (OtherActivityViewModel otherActivity : personalActivitiesMap.values()) {
				computeClassesForDiscipline(otherActivity);
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
		firebaseApi.getCourseService()
				.getAll()
				.onComplete(receivedCourses -> {
							courseMap = new BindableHashMap<>(binding, courses, receivedCourses);
							computePersonalCourses();
						},
						error -> activity.logAndShowErrorSnack("An error occurred while loading activities.", error, LOGGER));
	}

	private void loadOtherActivities() {
		firebaseApi.getOtherActivityService()
				.getAll()
				.onComplete(receivedOtherActivities -> {
							otherActivityMap = new BindableHashMap<>(binding, otherActivities, receivedOtherActivities);
							computePersonalActivities();
						},
						error -> activity.logAndShowErrorSnack("An error occurred while loading activities.", error, LOGGER));
	}

	private void loadProfessors() {
		firebaseApi.getProfessorService()
				.getAll()
				.onComplete(receivedProfessors -> professorsMap = new BindableHashMap<>(binding, professors, receivedProfessors),
						error -> activity.logAndShowErrorSnack("An error occurred while loading professors.", error, LOGGER));
	}

	private void loadRecurringClasses() {
		for (String recurringClassKey : recurringClassKeys) {
			firebaseApi.getRecurringClassService()
					.getById(recurringClassKey, true)
					.onSuccess(recurringClass -> {
						if (recurringClassKey.contains(recurringClass.getKey())) {
							recurringClassMap.put(recurringClass);
						}
						else {
							recurringClassMap.remove(recurringClass);
						}
					})
					.onError(error -> activity.logAndShowErrorSnack("An error occurred while loading regular classes.", error, LOGGER));
		}
	}

	private void loadOneTimeClasses() {
		for (String oneTimeClassKey : oneTimeClassKeys) {
			firebaseApi.getOneTimeClassService()
					.getById(oneTimeClassKey, true)
					.onSuccess(oneTimeClass -> {
						if (oneTimeClassKeys.contains(oneTimeClass.getKey())) {
							oneTimeClassMap.put(oneTimeClass);
						}
						else {
							oneTimeClassMap.remove(oneTimeClass);
						}
					})
					.onError(error -> activity.logAndShowErrorSnack("An error occurred while loading special classes.", error, LOGGER));
		}
	}

	private void loadStudent(String key) {
		firebaseApi.getStudentService()
				.getById(key, true)
				.onSuccess(student -> {
					currentProfile = student.clone();
					binding.setStudent(student);
					computePersonalCourses();
					computePersonalActivities();

					if (student.getImageMetadataKey() == null ||
							student.getImageMetadataKey().length() == 0) {
						binding.setProfileImageContent(null);
					}
					else if (binding.getStudent() == null ||
							binding.getStudent()
									.getImageMetadataKey() == null ||
							!binding.getStudent()
									.getImageMetadataKey()
									.equals(student.getImageMetadataKey())) {
						loadImage(student.getImageMetadataKey());
					}
				})
				.onError(error -> activity.logAndShowErrorSnack("Error loading your personal data", error, LOGGER));
	}

	private void loadImage(String profileImageKey) {
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

	public void update(StudentViewModel studentViewModel) {
		firebaseApi.getStudentService()
				.save(studentViewModel)
				.onSuccess(none -> activity.showSnackBar("Successfully updated your profile!", 1000));
	}
}
