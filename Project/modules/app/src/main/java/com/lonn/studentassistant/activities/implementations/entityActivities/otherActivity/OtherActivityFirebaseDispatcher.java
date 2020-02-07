package com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity;

import com.lonn.studentassistant.activities.abstractions.EntityActivityDispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.OtherActivityService;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.functionalIntefaces.Function;
import com.lonn.studentassistant.logging.Logger;

import java.util.LinkedList;
import java.util.List;

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
		firebaseApi.getAuthenticationService()
				.setOnLoggedPersonChange(person -> {
					entityActivity.updateBindingVariables();

					filesMap.clear();
					studentMap.clear();

					updateFiles(entityActivity.getActivityEntity());
					updateStudents(entityActivity.getActivityEntity());
				});

		firebaseApi.getOtherActivityService()
				.getById(entityKey, true)
				.onSuccess(activity -> {
					currentEntity = activity.clone();
					entityActivity.setActivityEntity(activity);
					entityActivity.updateBindingVariables();

					updateProfessors(activity);
					updateRecurringClasses(activity);
					updateOneTimeClasses(activity);
					updateFiles(activity);
					updateStudents(activity);

					entityActivity.updateBindingVariables();
				})
				.onError(error -> activity.logAndShowErrorSnack("An error occurred while loading the activity.",
						new Exception("Loading activity: " + error.getMessage()),
						LOGGER));
	}

	private void updateStudents(OtherActivityViewModel otherActivity) {
		updateCourseRelatedEntities(studentMap,
				crs -> {
					List<String> allStudents = new LinkedList<>(crs.getStudents());
					allStudents.addAll(crs.getPendingStudents());
					return allStudents;
				},
				firebaseApi.getStudentService(),
				otherActivity);
	}

	private void updateProfessors(OtherActivityViewModel otherActivity) {
		updateCourseRelatedEntities(professorMap,
				OtherActivityViewModel::getProfessors,
				firebaseApi.getProfessorService(),
				otherActivity);
	}

	private void updateOneTimeClasses(OtherActivityViewModel otherActivity) {
		updateCourseRelatedEntities(oneTimeClassesMap,
				OtherActivityViewModel::getOneTimeClasses,
				firebaseApi.getOneTimeClassService(),
				otherActivity);
	}

	private void updateRecurringClasses(OtherActivityViewModel otherActivity) {
		updateCourseRelatedEntities(recurringClassesMap,
				OtherActivityViewModel::getRecurringClasses,
				firebaseApi.getRecurringClassService(),
				otherActivity);
	}

	private void updateFiles(OtherActivityViewModel otherActivity) {
		updateCourseRelatedEntities(filesMap,
				OtherActivityViewModel::getFileMetadataKeys,
				firebaseApi.getFileMetadataService(),
				otherActivity);
	}

	private <T extends BaseEntity, V extends EntityViewModel<T>> void updateCourseRelatedEntities(BindableHashMap<V> entityMap,
																								  Function<OtherActivityViewModel,
																										  List<String>> entityKeyGetter,
																								  Service<T, Exception, V> entityService,
																								  OtherActivityViewModel newActivity) {
		removeNonExistingEntities(entityMap, entityKeyGetter.apply(newActivity));

		for (String entityKey : entityKeyGetter.apply(newActivity)) {
			if (!entityMap.keySet().contains(entityKey)) {
				entityService.getById(entityKey, true)
						.onSuccess(entity -> {
							if (entityKeyGetter.apply(entityActivity.getBinding().getEntity())
									.contains(entity.getKey())) {
								entityMap.put(entity);
							}
						})
						.onError(error -> entityMap.remove(entityKey));
			}
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

