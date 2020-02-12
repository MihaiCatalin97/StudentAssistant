package com.lonn.studentassistant.activities.implementations.professor;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.databinding.ProfessorActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AnnouncementViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.BR.administrativeFiles;
import static com.lonn.studentassistant.BR.announcements;
import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.otherActivities;
import static com.lonn.studentassistant.BR.personalCourses;
import static com.lonn.studentassistant.BR.personalFiles;
import static com.lonn.studentassistant.BR.personalOtherActivities;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.BR.recurringClasses;
import static com.lonn.studentassistant.utils.schedule.Utils.getNextClass;
import static com.lonn.studentassistant.utils.schedule.Utils.getNextExam;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidEmail;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidName;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidPhoneNumber;

class ProfessorActivityFirebaseDispatcher extends Dispatcher<ProfessorActivity, ProfessorViewModel> {
    private static final Logger LOGGER = Logger.ofClass(ProfessorActivityFirebaseDispatcher.class);
    private ProfessorActivityMainLayoutBinding binding;

    private BindableHashMap<CourseViewModel> courseMap;
    private BindableHashMap<OtherActivityViewModel> otherActivityMap;
    private BindableHashMap<ProfessorViewModel> professorsMap;

    private BindableHashMap<RecurringClassViewModel> recurringClassesMap;
    private BindableHashMap<OneTimeClassViewModel> oneTimeClassesMap;
    private BindableHashMap<CourseViewModel> personalCoursesMap;
    private BindableHashMap<OtherActivityViewModel> personalActivitiesMap;
    private BindableHashMap<FileMetadataViewModel> personalFilesMap;
    private BindableHashMap<AnnouncementViewModel> announcementMap;
    private BindableHashMap<FileMetadataViewModel> administrativeFilesMap;

    ProfessorActivityFirebaseDispatcher(ProfessorActivity professorActivity) {
        super(professorActivity);
        this.binding = professorActivity.binding;

        courseMap = new BindableHashMap<>(binding, courses);
        otherActivityMap = new BindableHashMap<>(binding, otherActivities);
        professorsMap = new BindableHashMap<>(binding, professors);

        recurringClassesMap = new BindableHashMap<>(binding, recurringClasses);
        oneTimeClassesMap = new BindableHashMap<>(binding, oneTimeClasses);
        personalCoursesMap = new BindableHashMap<>(binding, personalCourses);
        personalActivitiesMap = new BindableHashMap<>(binding, personalOtherActivities);
        personalFilesMap = new BindableHashMap<>(binding, personalFiles);
    }

    public void loadAll(String entityKey) {
        firebaseApi.getProfessorService()
                .getById(entityKey, true)
                .onSuccess(professor -> {
                    currentProfile = professor.clone();

                    removeNonExistingEntities(recurringClassesMap, professor.getRecurringClasses());
                    removeNonExistingEntities(oneTimeClassesMap, professor.getOneTimeClasses());
                    removeNonExistingEntities(personalActivitiesMap, professor.getOtherActivities());
                    removeNonExistingEntities(personalCoursesMap, professor.getCourses());
                    removeNonExistingEntities(personalFilesMap, professor.getFileMetadataKeys());

                    List<String> recurringClassesToLoad = new ArrayList<>(professor.getRecurringClasses());
                    List<String> oneTimeClassesToLoad = new ArrayList<>(professor.getOneTimeClasses());
                    List<String> activitiesToLoad = new ArrayList<>(professor.getOtherActivities());
                    List<String> coursesToLoad = new ArrayList<>(professor.getCourses());
                    List<String> filesToLoad = new ArrayList<>(professor.getFileMetadataKeys());

                    if (binding.getProfessor() != null) {
                        recurringClassesToLoad.removeAll(binding.getProfessor().getRecurringClasses());
                        oneTimeClassesToLoad.removeAll(binding.getProfessor().getOneTimeClasses());
                        activitiesToLoad.removeAll(binding.getProfessor().getOtherActivities());
                        coursesToLoad.removeAll(binding.getProfessor().getCourses());
                        filesToLoad.removeAll(binding.getProfessor().getFileMetadataKeys());
                    }

                    loadRecurringClasses(recurringClassesToLoad);
                    loadOneTimeClasses(oneTimeClassesToLoad);
                    loadCourses(coursesToLoad);
                    loadOtherActivities(activitiesToLoad);
                    loadFiles(filesToLoad);

                    if (professor.getImageMetadataKey() == null ||
                            professor.getImageMetadataKey().length() == 0) {
                        binding.setProfileImageContent(null);
                    }
                    else if (binding.getProfessor() == null ||
                            binding.getProfessor()
                                    .getImageMetadataKey() == null ||
                            !binding.getProfessor()
                                    .getImageMetadataKey()
                                    .equals(professor.getImageMetadataKey())) {
                        loadImage(professor.getImageMetadataKey());
                    }

                    binding.setProfessor(professor);
                    setNextClasses();
                })
                .onError(error -> activity.logAndShowErrorSnack("An error occurred while loading the professor.",
                        new Exception("Loading professor: " + error.getMessage()),
                        LOGGER)
                );

        loadOtherActivities();
        loadCourses();
        loadProfessors();
        loadAnnouncements();
        loadAdministrativeFiles();
    }

    @Override
    public boolean update(ProfessorViewModel professorViewModel) {
        if (!isValidEmail(professorViewModel.getEmail())) {
            activity.showSnackBar("Invalid email!", 2000);
            return false;
        }
        if (!isValidPhoneNumber(professorViewModel.getPhoneNumber())) {
            activity.showSnackBar("Invalid phone number!", 2000);
            return false;
        }
        if (!isValidName(professorViewModel.getFirstName() + " " + professorViewModel.getLastName())) {
            activity.showSnackBar("Invalid name!", 2000);
            return false;
        }

        firebaseApi.getProfessorService()
                .save(professorViewModel)
                .onSuccess(none -> activity.showSnackBar("Successfully updated your profile!", 1000));

        return true;
    }

    private void setNextClasses() {
        activity.setNextClass(getNextClass(recurringClassesMap.values(),
                oneTimeClassesMap.values()));
        activity.setNextExam(getNextExam(oneTimeClassesMap.values()));
    }

    private void loadProfessors() {
        firebaseApi.getProfessorService()
                .getAll(true)
                .onSuccess(receivedProfessors -> professorsMap = new BindableHashMap<>(binding, professors, receivedProfessors))
                .onError(error -> activity.logAndShowErrorSnack("An error occurred while loading professors.", error, LOGGER));
    }

    private void loadFiles(List<String> fileIds) {
        for (String fileId : fileIds) {
            firebaseApi.getFileMetadataService()
                    .getById(fileId, true)
                    .onSuccess(personalFilesMap::put);
        }
    }

    private void loadCourses(List<String> courseIds) {
        for (String courseId : courseIds) {
            firebaseApi.getCourseService()
                    .getById(courseId, true)
                    .onSuccess(personalCoursesMap::put);
        }
    }

    private void loadOtherActivities(List<String> activityIds) {
        for (String activityId : activityIds) {
            firebaseApi.getOtherActivityService()
                    .getById(activityId, true)
                    .onSuccess(personalActivitiesMap::put);
        }
    }

    private void loadCourses() {
        firebaseApi.getCourseService()
                .getAll(true)
                .onSuccess(receivedCourses -> courseMap = new BindableHashMap<>(binding, courses, receivedCourses))
                .onError(error -> activity.logAndShowErrorSnack("An error occurred while loading courses.", error, LOGGER));
    }

    private void loadOtherActivities() {
        firebaseApi.getOtherActivityService()
                .getAll(true)
                .onSuccess(receivedOtherActivities -> otherActivityMap = new BindableHashMap<>(binding, otherActivities, receivedOtherActivities))
                .onError(error -> activity.logAndShowErrorSnack("An error occurred while loading activities.", error, LOGGER));
    }

    private void loadRecurringClasses(List<String> recurringClassIds) {
        for (String recurringClassId : recurringClassIds) {
            firebaseApi.getRecurringClassService()
                    .getById(recurringClassId, true)
                    .onSuccess(recurringClass -> {
                        if (binding.getProfessor().getRecurringClasses().contains(recurringClassId)) {
                            recurringClassesMap.put(recurringClass);
                        }
                        else {
                            recurringClassesMap.remove(recurringClassId);
                        }
                        setNextClasses();
                    });
        }
    }

    private void loadOneTimeClasses(List<String> oneTimeClassIds) {
        for (String oneTimeClassId : oneTimeClassIds) {
            firebaseApi.getOneTimeClassService()
                    .getById(oneTimeClassId, true)
                    .onSuccess(oneTimeClass -> {
                        if (binding.getProfessor().getOneTimeClasses().contains(oneTimeClassId)) {
                            oneTimeClassesMap.put(oneTimeClass);
                        }
                        else {
                            oneTimeClassesMap.remove(oneTimeClassId);
                        }
                        setNextClasses();
                    });
        }
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

    private void loadAnnouncements() {
        firebaseApi.getAnnouncementService()
                .getAll(true)
                .onSuccess(receivedAnnouncements -> announcementMap = new BindableHashMap<>(binding, announcements, receivedAnnouncements))
                .onError(error -> activity.logAndShowErrorSnack("An error occurred while loading announcements.", error, LOGGER));
    }

    private void loadAdministrativeFiles() {
        firebaseApi.getFileMetadataService()
                .getByAssociatedEntityKey("ADMINISTRATIVE", true)
                .onSuccess(receivedFiles ->
                        administrativeFilesMap = new BindableHashMap<>(binding, administrativeFiles, receivedFiles))
                .onError(error ->
                        activity.logAndShowErrorSnack("An error occurred while loading administrative files.", error, LOGGER));
    }
}
