package com.lonn.studentassistant.activities.implementations.administrator;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.AdministratorActivityMainLayoutBinding;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.logging.Logger;

import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.otherActivities;

class AdministratorActivityFirebaseDispatcher extends Dispatcher<AdministratorActivity> {
	private static final Logger LOGGER = Logger.ofClass(AdministratorActivityFirebaseDispatcher.class);
	private AdministratorActivityMainLayoutBinding binding;

	private BindableHashMap<CourseViewModel> courseMap;
	private BindableHashMap<OtherActivityViewModel> otherActivityMap;

	AdministratorActivityFirebaseDispatcher(AdministratorActivity administratorActivity) {
		super(administratorActivity);
		this.binding = administratorActivity.binding;

		courseMap = new BindableHashMap<>(binding, courses);
		otherActivityMap = new BindableHashMap<>(binding, otherActivities);
	}

	public void loadAll(String entityKey) {
		if (entityKey != null) {
			loadAdministrator(entityKey);
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
							},
							error -> activity.logAndShowErrorSnack("An error occurred while loading activities.", error, LOGGER));
		}
	}

	private void loadProfessors() {
		if (binding.getProfessors() == null) {
			firebaseApi.getProfessorService()
					.getAll()
					.onComplete(professors -> {
								binding.setProfessors(professors);
							},
							error -> activity.logAndShowErrorSnack("An error occurred while loading professors.", error, LOGGER));
		}
	}

	private void loadAdministrator(String key) {
		firebaseApi.getAdministratorService()
				.getById(key, true)
				.onSuccess(administrator -> {
					binding.setAdministrator(administrator);

					if (administrator.getImageMetadataKey() != null) {
						loadProfileImage(administrator.getImageMetadataKey());
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
