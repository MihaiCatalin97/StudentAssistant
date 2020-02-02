package com.lonn.studentassistant.activities.implementations.administrator;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.AdministratorActivityMainLayoutBinding;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.viewModels.AdministratorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.otherActivities;
import static com.lonn.studentassistant.BR.personalFiles;
import static com.lonn.studentassistant.BR.professors;

class AdministratorActivityFirebaseDispatcher extends Dispatcher<AdministratorActivity, AdministratorViewModel> {
	private static final Logger LOGGER = Logger.ofClass(AdministratorActivityFirebaseDispatcher.class);
	private AdministratorActivityMainLayoutBinding binding;

	private BindableHashMap<CourseViewModel> courseMap;
	private BindableHashMap<OtherActivityViewModel> otherActivityMap;
	private BindableHashMap<ProfessorViewModel> professorsMap;

	private BindableHashMap<FileMetadataViewModel> personalFilesMap;

	AdministratorActivityFirebaseDispatcher(AdministratorActivity administratorActivity) {
		super(administratorActivity);
		this.binding = administratorActivity.binding;

		courseMap = new BindableHashMap<>(binding, courses);
		otherActivityMap = new BindableHashMap<>(binding, otherActivities);
		professorsMap = new BindableHashMap<>(binding, professors);

		personalFilesMap = new BindableHashMap<>(binding, personalFiles);
	}

	public void loadAll(String entityKey) {
		firebaseApi.getAdministratorService()
				.getById(entityKey, true)
				.onSuccess(administrator -> {
					currentProfile = administrator.clone();

					removeNonExistingEntities(personalFilesMap, administrator.getFileMetadataKeys());

					List<String> filesToLoad = new ArrayList<>(administrator.getFileMetadataKeys());

					if (binding.getAdministrator() != null) {
						filesToLoad.removeAll(binding.getAdministrator().getFileMetadataKeys());
					}

					loadFiles(filesToLoad);

					if (administrator.getImageMetadataKey() == null ||
							administrator.getImageMetadataKey().length() == 0) {
						binding.setProfileImageContent(null);
					}
					else if (binding.getAdministrator() == null ||
							binding.getAdministrator()
									.getImageMetadataKey() == null ||
							!binding.getAdministrator()
									.getImageMetadataKey()
									.equals(administrator.getImageMetadataKey())) {
						loadImage(administrator.getImageMetadataKey());
					}

					binding.setAdministrator(administrator);
				})
				.onError(error -> activity.logAndShowErrorSnack("An error occurred while loading the professor.",
						new Exception("Loading professor: " + error.getMessage()),
						LOGGER)
				);

		loadOtherActivities();
		loadCourses();
		loadProfessors();
	}

	private void loadProfessors() {
		firebaseApi.getProfessorService()
				.getAll()
				.onComplete(receivedProfessors -> professorsMap = new BindableHashMap<>(binding, professors, receivedProfessors),
						error -> activity.logAndShowErrorSnack("An error occurred while loading professors.", error, LOGGER));
	}

	private void loadFiles(List<String> fileIds) {
		for (String fileId : fileIds) {
			firebaseApi.getFileMetadataService()
					.getById(fileId, true)
					.onSuccess(personalFilesMap::put);
		}
	}

	private void loadCourses() {
		firebaseApi.getCourseService()
				.getAll()
				.onComplete(receivedCourses -> courseMap = new BindableHashMap<>(binding, courses, receivedCourses),
						error -> activity.logAndShowErrorSnack("An error occurred while loading courses.", error, LOGGER));
	}

	private void loadOtherActivities() {
		firebaseApi.getOtherActivityService()
				.getAll()
				.onComplete(receivedOtherActivities -> otherActivityMap = new BindableHashMap<>(binding, otherActivities, receivedOtherActivities),
						error -> activity.logAndShowErrorSnack("An error occurred while loading activities.", error, LOGGER));
	}

	private void loadImage(String imageMetadataId) {
		if (imageMetadataId != null) {
			firebaseApi.getFileMetadataService()
					.getById(imageMetadataId, true)
					.onSuccess(metadata ->
							firebaseApi.getFileContentService()
									.getById(metadata.getFileContentKey(), true)
									.onSuccess(binding::setProfileImageContent)
									.onError(error -> activity.logAndShowErrorSnack(
											"Unable to load your profile image",
											error,
											LOGGER)))
					.onError(error -> activity.logAndShowErrorSnack(
							"Unable to load your profile image",
							error,
							LOGGER));
		}
	}

	public void update(AdministratorViewModel administratorViewModel) {
		firebaseApi.getAdministratorService()
				.save(administratorViewModel)
				.onSuccess(none -> activity.showSnackBar("Successfully updated your profile!", 1000));
	}
}
