package com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.databinding.OtherActivityEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.logging.Logger;

import static com.lonn.studentassistant.BR.files;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.BR.recurringClasses;

class OtherActivityFirebaseDispatcher extends Dispatcher {
	private static final Logger LOGGER = Logger.ofClass(OtherActivityFirebaseDispatcher.class);
	private OtherActivityEntityActivityLayoutBinding binding;
	private BindableHashMap<ProfessorViewModel> professorMap;
	private BindableHashMap<RecurringClassViewModel> recurringClassesMap;
	private BindableHashMap<OneTimeClassViewModel> oneTimeClassesMap;
	private BindableHashMap<FileMetadataViewModel> filesMap;

	OtherActivityFirebaseDispatcher(OtherActivityEntityActivity entityActivity) {
		super(entityActivity);
		this.binding = entityActivity.binding;

		professorMap = new BindableHashMap<>(binding, professors);
		recurringClassesMap = new BindableHashMap<>(binding, recurringClasses);
		oneTimeClassesMap = new BindableHashMap<>(binding, oneTimeClasses);
		filesMap = new BindableHashMap<>(binding, files);
	}

	void loadAll(String entityKey) {
		firebaseApi.getOtherActivityService()
				.getById(entityKey)
				.onComplete(activity -> {
							binding.setOtherActivity(activity);

							removeNonExistingEntities(professorMap, activity.getProfessors());
							removeNonExistingEntities(recurringClassesMap, activity.getRecurringClasses());
							removeNonExistingEntities(oneTimeClassesMap, activity.getOneTimeClasses());
							removeNonExistingEntities(filesMap, activity.getFilesMetadata());

							for (String professorId : activity.getProfessors()) {
								firebaseApi.getProfessorService()
										.getById(professorId)
										.onComplete(professorMap::put);
							}

							for (String recurringClassId : activity.getRecurringClasses()) {
								firebaseApi.getRecurringClassService()
										.getById(recurringClassId)
										.onComplete(recurringClassesMap::put);
							}

							for (String oneTimeClassId : activity.getOneTimeClasses()) {
								firebaseApi.getOneTimeClassService()
										.getById(oneTimeClassId)
										.onComplete(oneTimeClassesMap::put);
							}

							for (String fileId : activity.getFilesMetadata()) {
								firebaseApi.getFileMetadataService()
										.getById(fileId)
										.onComplete(filesMap::put);
							}

						},
						error -> activity.logAndShowError("An error occurred while loading the activity.",
								new Exception("Loading activity: " + error.getMessage()),
								LOGGER));
	}
}
