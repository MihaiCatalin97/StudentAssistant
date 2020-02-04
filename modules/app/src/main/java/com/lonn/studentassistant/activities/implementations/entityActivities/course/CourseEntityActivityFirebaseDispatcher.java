package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import com.lonn.studentassistant.activities.abstractions.EntityActivityDispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.services.CourseService;
import com.lonn.studentassistant.firebaselayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.functionalIntefaces.Function;
import com.lonn.studentassistant.logging.Logger;

import java.util.LinkedList;
import java.util.List;

import static com.lonn.studentassistant.BR.files;
import static com.lonn.studentassistant.BR.laboratories;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.BR.recurringClasses;

class CourseEntityActivityFirebaseDispatcher extends EntityActivityDispatcher<CourseViewModel> {
	private static final Logger LOGGER = Logger.ofClass(CourseEntityActivityFirebaseDispatcher.class);
	private BindableHashMap<ProfessorViewModel> professorMap;
	private BindableHashMap<RecurringClassViewModel> recurringClassesMap;
	private BindableHashMap<OneTimeClassViewModel> oneTimeClassesMap;
	private BindableHashMap<FileMetadataViewModel> filesMap;
	private BindableHashMap<LaboratoryViewModel> laboratoryMap;
	private BindableHashMap<StudentViewModel> studentMap;
	private CourseEntityActivity entityActivity;

	CourseEntityActivityFirebaseDispatcher(CourseEntityActivity entityActivity) {
		super(entityActivity);

		this.entityActivity = entityActivity;

		professorMap = new BindableHashMap<>(entityActivity.getBinding(), professors);
		recurringClassesMap = new BindableHashMap<>(entityActivity.getBinding(), recurringClasses);
		oneTimeClassesMap = new BindableHashMap<>(entityActivity.getBinding(), oneTimeClasses);
		filesMap = new BindableHashMap<>(entityActivity.getBinding(), files);
		laboratoryMap = new BindableHashMap<>(entityActivity.getBinding(), laboratories);
		studentMap = new BindableHashMap<>(entityActivity.getBinding(), com.lonn.studentassistant.BR.students);
	}

	void loadAll(String courseKey) {
		firebaseApi.getAuthenticationService()
				.setOnLoggedPersonChange(person -> {
					entityActivity.updateBindingVariables();

					filesMap.clear();
					studentMap.clear();

					updateFiles(entityActivity.getActivityEntity());
					updateStudents(entityActivity.getActivityEntity());
				});

		firebaseApi.getCourseService().getById(courseKey, true)
				.onSuccess(course -> {
					currentEntity = course.clone();
					activity.setActivityEntity(course);
					entityActivity.updateBindingVariables();

					updateProfessors(course);
					updateRecurringClasses(course);
					updateOneTimeClasses(course);
					updateFiles(course);
					updateStudents(course);
					updateLaboratories(course);

					entityActivity.updateBindingVariables();
				})
				.onError(error -> activity.logAndShowErrorSnack("An error occurred while loading the course.",
						new

								Exception("Loading course: " + error.getMessage()),
						LOGGER));
	}

	private void updateStudents(CourseViewModel course) {
		updateCourseRelatedEntities(studentMap,
				crs -> {
					List<String> allStudents = new LinkedList<>(crs.getStudents());
					allStudents.addAll(crs.getPendingStudents());
					return allStudents;
				},
				firebaseApi.getStudentService(),
				course);
	}

	private void updateProfessors(CourseViewModel course) {
		updateCourseRelatedEntities(professorMap,
				CourseViewModel::getProfessors,
				firebaseApi.getProfessorService(),
				course);
	}

	private void updateLaboratories(CourseViewModel course) {
		updateCourseRelatedEntities(laboratoryMap,
				CourseViewModel::getLaboratories,
				firebaseApi.getLaboratoryService(),
				course);
	}

	private void updateOneTimeClasses(CourseViewModel course) {
		updateCourseRelatedEntities(oneTimeClassesMap,
				CourseViewModel::getOneTimeClasses,
				firebaseApi.getOneTimeClassService(),
				course);
	}

	private void updateRecurringClasses(CourseViewModel course) {
		updateCourseRelatedEntities(recurringClassesMap,
				CourseViewModel::getRecurringClasses,
				firebaseApi.getRecurringClassService(),
				course);
	}

	private void updateFiles(CourseViewModel course) {
		updateCourseRelatedEntities(filesMap,
				CourseViewModel::getFileMetadataKeys,
				firebaseApi.getFileMetadataService(),
				course);
	}

	private <T extends BaseEntity, V extends EntityViewModel<T>> void updateCourseRelatedEntities(BindableHashMap<V> entityMap,
																								  Function<CourseViewModel,
																										  List<String>> entityKeyGetter,
																								  Service<T, Exception, V> entityService,
																								  CourseViewModel newCourse) {
		removeNonExistingEntities(entityMap, entityKeyGetter.apply(newCourse));

		for (String entityKey : entityKeyGetter.apply(newCourse)) {
			if (!entityMap.keySet().contains(entityKey)) {
				entityService.getById(entityKey, true)
						.onSuccess(entity -> {
							if (entityKeyGetter.apply(entityActivity.getBinding().getEntity())
									.contains(entity.getKey())) {
								entityMap.put(entity);
							}
						});
			}
		}
	}

	@Override
	public CourseService getService() {
		return firebaseApi.getCourseService();
	}

	@Override
	public String getEntityName() {
		return "course";
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}
}
