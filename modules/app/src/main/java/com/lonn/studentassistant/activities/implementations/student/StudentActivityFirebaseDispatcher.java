package com.lonn.studentassistant.activities.implementations.student;

import android.util.Log;

import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.HashMap;
import java.util.Map;

class StudentActivityFirebaseDispatcher {
	private static final Logger LOGGER = Logger.ofClass(StudentActivityFirebaseDispatcher.class);
	private StudentActivityMainLayoutBinding binding;
	private FirebaseApi firebaseApi;
	private StudentActivity studentActivity;

	StudentActivityFirebaseDispatcher(StudentActivity studentActivity) {
		this.studentActivity = studentActivity;
		binding = studentActivity.binding;
		firebaseApi = studentActivity.getFirebaseApi();
	}

	void loadCourses() {
		if (binding.getCourses() == null) {
			firebaseApi.getCourseService()
					.getAll()
					.onComplete(binding::setCourses,
							error -> studentActivity.logAndShowError(error.getMessage(), error, LOGGER));
		}
	}

	void loadOtherActivities() {
		if (binding.getOtherActivities() == null) {
			firebaseApi.getOtherActivityService()
					.getAll()
					.onComplete(binding::setOtherActivities,
							error -> {
								Log.e("Loading activities", error.getMessage() == null ? "no message" : error.getMessage());
								studentActivity.showSnackBar("An error occurred while loading activities.");
							});
		}
	}

	void loadProfessors() {
		if (binding.getProfessors() == null) {
			firebaseApi.getProfessorService()
					.getAll()
					.onComplete(binding::setProfessors,
							error -> {
								Log.e("Loading professors", error.getMessage() == null ? "no message" : error.getMessage());
								studentActivity.showSnackBar("An error occurred while loading professors.");
							});
		}
	}

	void loadRecurringClasses(StudentViewModel studentViewModel) {
		if (binding.getRecurringClasses() == null) {
			firebaseApi.getRecurringClassService()
					.getAll()
					.onComplete(receivedRecurringClasses -> {
								Map<String, RecurringClassViewModel> recurringClassViewModels = new HashMap<>();

								for (RecurringClassViewModel recurringClass : receivedRecurringClasses) {
									if (isPersonalScheduleClass(recurringClass, studentViewModel)) {
										recurringClassViewModels.put(recurringClass.getKey(),
												recurringClass);
									}
								}

								binding.setRecurringClasses(recurringClassViewModels);
							},
							error -> {
								Log.e("Loading R. classes", error.getMessage() == null ? "no message" : error.getMessage());
								studentActivity.showSnackBar("An error occurred while loading regular classes.");
							});
		}
	}

	void loadOneTimeClasses(StudentViewModel studentViewModel) {
		if (binding.getOneTimeClasses() == null) {
			firebaseApi.getOneTimeClassService()
					.getAll()
					.onComplete(receivedOneTimeClasses -> {
								Map<String, OneTimeClassViewModel> oneTimeClasses = new HashMap<>();

								for (OneTimeClassViewModel oneTimeClassViewModel : receivedOneTimeClasses) {
									if (isPersonalScheduleClass(oneTimeClassViewModel, studentViewModel)) {
										oneTimeClasses.put(oneTimeClassViewModel.getKey(),
												oneTimeClassViewModel);
									}
								}

								binding.setOneTimeClasses(oneTimeClasses);
							},
							error -> {
								Log.e("Loading OT. classes", error.getMessage() == null ? "no message" : error.getMessage());
								studentActivity.showSnackBar("An error occurred while loading special classes.");
							});
		}
	}

	private boolean isPersonalScheduleClass(ScheduleClassViewModel scheduleClass,
											StudentViewModel studentViewModel) {
		String year = studentViewModel.getCycleSpecialization()
				.getInitials() + studentViewModel.getYear();

		String semiYear = year + studentViewModel.getGroup().charAt(0);
		String group = year + studentViewModel.getGroup();

		return scheduleClass.getGroups().contains(year) ||
				scheduleClass.getGroups().contains(semiYear) ||
				scheduleClass.getGroups().contains(group);
	}
}
