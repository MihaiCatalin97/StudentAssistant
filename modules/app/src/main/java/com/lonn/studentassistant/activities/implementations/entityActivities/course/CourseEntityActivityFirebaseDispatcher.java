package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.logging.Logger;

import static com.lonn.studentassistant.BR.files;
import static com.lonn.studentassistant.BR.laboratories;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.BR.recurringClasses;

class CourseEntityActivityFirebaseDispatcher extends Dispatcher {
    private static final Logger LOGGER = Logger.ofClass(CourseEntityActivityFirebaseDispatcher.class);
    private CourseEntityActivityLayoutBinding binding;
    private BindableHashMap<ProfessorViewModel> professorMap;
    private BindableHashMap<RecurringClassViewModel> recurringClassesMap;
    private BindableHashMap<OneTimeClassViewModel> oneTimeClassesMap;
    private BindableHashMap<FileMetadataViewModel> filesMap;
    private BindableHashMap<LaboratoryViewModel> laboratoryMap;

    CourseEntityActivityFirebaseDispatcher(CourseEntityActivity entityActivity) {
        super(entityActivity);
        this.binding = entityActivity.binding;

        professorMap = new BindableHashMap<>(binding, professors);
        recurringClassesMap = new BindableHashMap<>(binding, recurringClasses);
        oneTimeClassesMap = new BindableHashMap<>(binding, oneTimeClasses);
        filesMap = new BindableHashMap<>(binding, files);
        laboratoryMap = new BindableHashMap<>(binding, laboratories);
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

            binding.setCourse(course);
        })
                .onError(error -> activity.logAndShowErrorSnack("An error occurred while loading the course.",
                        new

                                Exception("Loading course: " + error.getMessage()),
                        LOGGER));
    }

    private boolean shouldUpdateProfessors(CourseViewModel course) {
        return this.binding.getProfessors() == null ||
                this.binding.getProfessors().size() != course.getProfessors().size() ||
                !this.binding.getProfessors().keySet()
                        .containsAll(course.getProfessors());
    }

    private boolean shouldUpdateLaboratories(CourseViewModel course) {
        return this.binding.getLaboratories() == null ||
                this.binding.getLaboratories().size() != course.getLaboratories().size() ||
                !this.binding.getLaboratories().keySet()
                        .containsAll(course.getLaboratories());
    }

    private boolean shouldUpdateOneTimeClasses(CourseViewModel course) {
        return this.binding.getOneTimeClasses() == null ||
                this.binding.getOneTimeClasses().size() != course.getOneTimeClasses().size() ||
                !this.binding.getOneTimeClasses().keySet()
                        .containsAll(course.getOneTimeClasses());
    }

    private boolean shouldUpdateRecurringClasses(CourseViewModel course) {
        return this.binding.getRecurringClasses() == null ||
                this.binding.getRecurringClasses().size() != course.getRecurringClasses().size() ||
                !this.binding.getRecurringClasses().keySet()
                        .containsAll(course.getRecurringClasses());
    }

    private boolean shouldUpdateFiles(CourseViewModel course) {
        return this.binding.getFiles() == null ||
                this.binding.getFiles().size() != course.getFileMetadataKeys().size() ||
                !this.binding.getFiles().keySet()
                        .containsAll(course.getFileMetadataKeys());
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
}
