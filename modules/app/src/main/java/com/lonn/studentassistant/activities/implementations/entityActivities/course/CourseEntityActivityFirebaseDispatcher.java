package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import com.lonn.studentassistant.activities.abstractions.EntityActivityDispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.services.CourseService;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.logging.Logger;

import static com.lonn.studentassistant.BR.files;
import static com.lonn.studentassistant.BR.laboratories;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.BR.recurringClasses;
import static com.lonn.studentassistant.BR.students;

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
		studentMap = new BindableHashMap<>(entityActivity.getBinding(), students);
	}

	void loadAll(String courseKey) {
		firebaseApi.getCourseService().getById(courseKey, true).onSuccess(course -> {
			if (shouldUpdateProfessors(course)) {
				updateProfessors(course);
			}

			if (shouldUpdateRecurringClasses(course)) {
				updateRecurringClasses(course);
			}

			if (shouldUpdateOneTimeClasses(course)) {
				updateOneTimeClasses(course);
			}

			if (shouldUpdateFiles(course)) {
				updateFiles(course);
			}

			if (shouldUpdateLaboratories(course)) {
				updateLaboratories(course);
			}

			if (shouldUpdateStudents(course)) {
				updateStudents(course);
			}

			activity.setActivityEntity(course);
		})
				.onError(error -> activity.logAndShowErrorSnack("An error occurred while loading the course.",
						new

								Exception("Loading course: " + error.getMessage()),
						LOGGER));
	}

	private boolean shouldUpdateProfessors(CourseViewModel course) {
		return this.entityActivity.getBinding().getProfessors() == null ||
				this.entityActivity.getBinding().getProfessors().size() != course.getProfessors().size() ||
				!this.entityActivity.getBinding().getProfessors().keySet()
						.containsAll(course.getProfessors());
	}

	private boolean shouldUpdateLaboratories(CourseViewModel course) {
		return this.entityActivity.getBinding().getLaboratories() == null ||
				this.entityActivity.getBinding().getLaboratories().size() != course.getLaboratories().size() ||
				!this.entityActivity.getBinding().getLaboratories().keySet()
						.containsAll(course.getLaboratories());
	}

	private boolean shouldUpdateOneTimeClasses(CourseViewModel course) {
		return this.entityActivity.getBinding().getOneTimeClasses() == null ||
				this.entityActivity.getBinding().getOneTimeClasses().size() != course.getOneTimeClasses().size() ||
				!this.entityActivity.getBinding().getOneTimeClasses().keySet()
						.containsAll(course.getOneTimeClasses());
	}

	private boolean shouldUpdateRecurringClasses(CourseViewModel course) {
		return this.entityActivity.getBinding().getRecurringClasses() == null ||
				this.entityActivity.getBinding().getRecurringClasses().size() != course.getRecurringClasses().size() ||
				!this.entityActivity.getBinding().getRecurringClasses().keySet()
						.containsAll(course.getRecurringClasses());
	}

	private boolean shouldUpdateFiles(CourseViewModel course) {
		return this.entityActivity.getBinding().getFiles() == null ||
				this.entityActivity.getBinding().getFiles().size() != course.getFileMetadataKeys().size() ||
				!this.entityActivity.getBinding().getFiles().keySet()
						.containsAll(course.getFileMetadataKeys());
	}

	private boolean shouldUpdateStudents(CourseViewModel course) {
		return this.entityActivity.getBinding().getStudents() == null ||
				this.entityActivity.getBinding().getStudents().size() != course.getStudents().size() ||
				!this.entityActivity.getBinding().getStudents().keySet()
						.containsAll(course.getStudents());
	}

	private void updateStudents(CourseViewModel course) {
		removeNonExistingEntities(studentMap, course.getStudents());
		for (String studentKey : course.getStudents()) {
			firebaseApi.getStudentService()
					.getById(studentKey, true)
					.onSuccess(studentMap::put);
		}
	}

	private void updateProfessors(CourseViewModel course) {
		removeNonExistingEntities(professorMap, course.getProfessors());
		for (String professorId : course.getProfessors()) {
			firebaseApi.getProfessorService()
					.getById(professorId, true)
					.onSuccess(professorMap::put);
		}
	}

	private void updateLaboratories(CourseViewModel course) {
		removeNonExistingEntities(laboratoryMap, course.getLaboratories());
		for (String laboratoryId : course.getLaboratories()) {
			firebaseApi.getLaboratoryService()
					.getById(laboratoryId, true)
					.onSuccess(laboratoryMap::put);
		}
	}

	private void updateOneTimeClasses(CourseViewModel course) {
		removeNonExistingEntities(oneTimeClassesMap, course.getOneTimeClasses());
		for (String oneTimeClassId : course.getOneTimeClasses()) {
			firebaseApi.getOneTimeClassService()
					.getById(oneTimeClassId, true)
					.onSuccess(oneTimeClassesMap::put);
		}
	}

	private void updateRecurringClasses(CourseViewModel course) {
		removeNonExistingEntities(recurringClassesMap, course.getRecurringClasses());
		for (String recurringClassId : course.getRecurringClasses()) {
			firebaseApi.getRecurringClassService()
					.getById(recurringClassId, true)
					.onSuccess(recurringClassesMap::put);
		}
	}

	private void updateFiles(CourseViewModel course) {
		removeNonExistingEntities(filesMap, course.getFileMetadataKeys());
		for (String fileId : course.getFileMetadataKeys()) {
			firebaseApi.getFileMetadataService()
					.getById(fileId, true)
					.onSuccess(filesMap::put);
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
