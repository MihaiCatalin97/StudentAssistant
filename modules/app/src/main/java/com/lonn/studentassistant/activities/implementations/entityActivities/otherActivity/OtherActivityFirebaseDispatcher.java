package com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity;

import com.lonn.studentassistant.activities.abstractions.EntityActivityDispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.services.OtherActivityService;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.logging.Logger;

import static com.lonn.studentassistant.BR.files;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.BR.recurringClasses;
import static com.lonn.studentassistant.BR.students;

class OtherActivityFirebaseDispatcher extends EntityActivityDispatcher<OtherActivityViewModel> {
	private static final Logger LOGGER = Logger.ofClass(OtherActivityFirebaseDispatcher.class);
	private OtherActivityEntityActivity entityActivity;
	private BindableHashMap<ProfessorViewModel> professorMap;
	private BindableHashMap<RecurringClassViewModel> recurringClassesMap;
	private BindableHashMap<OneTimeClassViewModel> oneTimeClassesMap;
	private BindableHashMap<FileMetadataViewModel> filesMap;
	private BindableHashMap<StudentViewModel> studentMap;

	OtherActivityFirebaseDispatcher(OtherActivityEntityActivity entityActivity) {
		super(entityActivity);
		this.entityActivity = entityActivity;

		professorMap = new BindableHashMap<>(entityActivity.getBinding(), professors);
		recurringClassesMap = new BindableHashMap<>(entityActivity.getBinding(), recurringClasses);
		oneTimeClassesMap = new BindableHashMap<>(entityActivity.getBinding(), oneTimeClasses);
		filesMap = new BindableHashMap<>(entityActivity.getBinding(), files);
		studentMap = new BindableHashMap<>(entityActivity.getBinding(), students);
	}

	void loadAll(String entityKey) {
		firebaseApi.getOtherActivityService()
				.getById(entityKey, true)
				.onSuccess(activity -> {
					entityActivity.setActivityEntity(activity);

					removeNonExistingEntities(professorMap, activity.getProfessors());
					removeNonExistingEntities(recurringClassesMap, activity.getRecurringClasses());
					removeNonExistingEntities(oneTimeClassesMap, activity.getOneTimeClasses());
					removeNonExistingEntities(filesMap, activity.getFileMetadataKeys());

					for (String professorId : activity.getProfessors()) {
						firebaseApi.getProfessorService()
								.getById(professorId, true)
								.onSuccess(professorMap::put);
					}

					for (String recurringClassId : activity.getRecurringClasses()) {
						firebaseApi.getRecurringClassService()
								.getById(recurringClassId, true)
								.onSuccess(recurringClassesMap::put);
					}

					for (String oneTimeClassId : activity.getOneTimeClasses()) {
						firebaseApi.getOneTimeClassService()
								.getById(oneTimeClassId, true)
								.onSuccess(oneTimeClassesMap::put);
					}

					for (String fileId : activity.getFileMetadataKeys()) {
						firebaseApi.getFileMetadataService()
								.getById(fileId, true)
								.onSuccess(filesMap::put);
					}

					if (shouldUpdateStudents(activity)) {
						updateStudents(activity);
					}

				})
				.onError(error -> activity.logAndShowErrorSnack("An error occurred while loading the activity.",
						new Exception("Loading activity: " + error.getMessage()),
						LOGGER));
	}

	private boolean shouldUpdateStudents(OtherActivityViewModel activity) {
		return this.entityActivity.getBinding().getStudents() == null ||
				this.entityActivity.getBinding().getStudents().size() != activity.getStudents().size() ||
				!this.entityActivity.getBinding().getStudents().keySet()
						.containsAll(activity.getStudents());
	}

	private void updateStudents(OtherActivityViewModel activity) {
		removeNonExistingEntities(studentMap, activity.getStudents());
		for (String studentKey : activity.getStudents()) {
			firebaseApi.getStudentService()
					.getById(studentKey, true)
					.onSuccess(student -> {
						if (activity.getStudents().contains(student.getKey())) {
							studentMap.put(student);
						}
					});
		}
	}

	@Override
	public OtherActivityService getService() {
		return firebaseApi.getOtherActivityService();
	}

	@Override
	public String getEntityName() {
		return "activity";
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}
}

