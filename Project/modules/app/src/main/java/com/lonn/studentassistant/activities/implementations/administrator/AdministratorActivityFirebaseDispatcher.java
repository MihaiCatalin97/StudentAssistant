package com.lonn.studentassistant.activities.implementations.administrator;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.AdministratorActivityMainLayoutBinding;
import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AdministratorViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AnnouncementViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.BR.administrativeFiles;
import static com.lonn.studentassistant.BR.announcements;
import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.otherActivities;
import static com.lonn.studentassistant.BR.personalFiles;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidEmail;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidName;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidPhoneNumber;

class AdministratorActivityFirebaseDispatcher extends Dispatcher<AdministratorActivity, AdministratorViewModel> {
    private static final Logger LOGGER = Logger.ofClass(AdministratorActivityFirebaseDispatcher.class);
    private AdministratorActivityMainLayoutBinding binding;

    private BindableHashMap<CourseViewModel> courseMap;
    private BindableHashMap<OtherActivityViewModel> otherActivityMap;
    private BindableHashMap<ProfessorViewModel> professorsMap;
    private BindableHashMap<AnnouncementViewModel> announcementMap;
    private BindableHashMap<FileMetadataViewModel> administrativeFilesMap;

    private BindableHashMap<FileMetadataViewModel> personalFilesMap;

    AdministratorActivityFirebaseDispatcher(AdministratorActivity administratorActivity) {
        super(administratorActivity);
        this.binding = administratorActivity.binding;

        courseMap = new BindableHashMap<>(binding, courses);
        otherActivityMap = new BindableHashMap<>(binding, otherActivities);
        professorsMap = new BindableHashMap<>(binding, professors);
        announcementMap = new BindableHashMap<>(binding, announcements);
        administrativeFilesMap = new BindableHashMap<>(binding, administrativeFiles);

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
        loadAnnouncements();
        loadAdministrativeFiles();
    }

    @Override
    public boolean update(AdministratorViewModel administratorViewModel) {
        if (!isValidEmail(administratorViewModel.getEmail())) {
            activity.showSnackBar("Invalid email!", 2000);
            return false;
        }
        if (!isValidPhoneNumber(administratorViewModel.getPhoneNumber())) {
            activity.showSnackBar("Invalid phone number!", 2000);
            return false;
        }
        if (!isValidName(administratorViewModel.getFirstName() + " " + administratorViewModel.getLastName())) {
            activity.showSnackBar("Invalid name!", 2000);
            return false;
        }


        firebaseApi.getAdministratorService()
                .save(administratorViewModel)
                .onSuccess(none -> activity.showSnackBar("Successfully updated your profile!", 1000));

        return true;
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
}
