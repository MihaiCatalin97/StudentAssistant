package com.lonn.studentassistant.activities.implementations.administrator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.scheduleparser.ParseResult;
import com.lonn.scheduleparser.UAICScheduleParser;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.activities.abstractions.MainActivity;
import com.lonn.studentassistant.databinding.AdministratorActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AdministratorViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AnnouncementViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.AnnouncementInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.disciplines.ActivityInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.disciplines.CourseInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.AdministratorFileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.AdministratorImageUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.TargetedFileUploadDialog;

import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.Getter;

import static android.view.View.GONE;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.ADMINISTRATOR;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.PROFESSOR;
import static com.lonn.studentassistant.validation.Regex.EMAIL_REGEX;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class AdministratorActivity extends MainActivity<AdministratorViewModel> {
    private static final Logger LOGGER = Logger.ofClass(AdministratorActivity.class);
    protected Dispatcher<AdministratorActivity, AdministratorViewModel> dispatcher;
    @Getter
    AdministratorActivityMainLayoutBinding binding;
    private Handler parsingHandler = new Handler();
    private Runnable parsingResultRunnable = () -> {
        parsingHandler.removeCallbacksAndMessages(null);

        parsingHandler.postDelayed(() -> {
            showSnackBar("Successfully parsed UAIC schedule", 1500);
        }, 1500);
    };

    @Override
    public void onBackPressed() {
        if (binding.getEditingProfile() != null &&
                binding.getEditingProfile()) {
            binding.setEditingProfile(false);
        }
        else {
            super.onBackPressed();
        }
    }

    public void createRegistrationToken(View view) {
        AccountType accountType = getSelectedAccountType();
        String email = ((EditText) findViewById(R.id.registrationTokenEmailEditText)).getText().toString();

        if (!email.matches(EMAIL_REGEX)) {
            showSnackBar("Invalid email", 1000);
            return;
        }

        showSnackBar("Creating and sending token to " + email);
        firebaseApi.getRegistrationTokenService()
                .createTokenAndSendEmail(accountType, email)
                .onSuccess(none -> {
                    showSnackBar("The token has been created and send to " + email, 1000);
                    ((EditText) findViewById(R.id.registrationTokenEmailEditText)).setText("");
                })
                .onError(error -> logAndShowErrorSnack("An error occurred!",
                        error,
                        LOGGER));
    }

    public void onEditTapped() {
        boolean editing = binding.getEditingProfile() == null ? false : binding.getEditingProfile();

        binding.setEditingProfile(!editing);
    }

    public void onSaveTapped() {
        hideKeyboard();

        if (dispatcher.update(binding.getAdministrator())) {
            binding.setEditingProfile(false);
        }
    }

    public void parseSchedule(View v) {
        UAICScheduleParser uaicScheduleParser = new UAICScheduleParser();

        newSingleThreadExecutor().submit(() -> {
            showSnackBar("Parsing UAIC schedule");

            try {
                ParseResult parseResult = uaicScheduleParser.parseUAICSchedule().get();

                saveCourses(parseResult.getCourses());
                saveProfessors(parseResult.getProfessors());
                saveOtherActivities(parseResult.getOtherActivities());
                saveOneTimeClasses(parseResult.getOneTimeClasses());
                saveRecurringClasses(parseResult.getRecurringClasses());
            } catch (InterruptedException | ExecutionException exception) {
                showSnackBar("An error occurred while parsing the schedule!",
                        1000);
                LOGGER.error("Failed to parse UAIC schedule", exception);
            }
        });
    }

    protected void loadAll(String entityKey) {
        dispatcher.loadAll(entityKey);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatcher = new AdministratorActivityFirebaseDispatcher(this);

        findViewById(R.id.fabEdit).setOnClickListener(view -> onEditTapped());
        findViewById(R.id.fabSaveChanges).setOnClickListener(view -> onSaveTapped());
        findViewById(R.id.fabDiscardChanges).setOnClickListener(view -> onDiscardTapped());
        findViewById(R.id.fabDelete).setVisibility(GONE);

        ((ScrollViewCategory) findViewById(R.id.coursesCategory)).setOnAddAction(() -> {
            new CourseInputDialog(this)
                    .setPositiveButtonAction(course -> {
                        firebaseApi.getCourseService()
                                .save(course)
                                .onSuccess(none -> showSnackBar("Successfully created course", 1500))
                                .onError(error -> logAndShowErrorSnack("An error occurred while creating the course",
                                        error,
                                        LOGGER));
                    })
                    .show();
        });

        ((ScrollViewCategory) findViewById(R.id.activitiesCategory)).setOnAddAction(() -> {
            new ActivityInputDialog(this)
                    .setPositiveButtonAction(activity -> {
                        firebaseApi.getOtherActivityService()
                                .save(activity)
                                .onSuccess(none -> showSnackBar("Successfully created activity", 1500))
                                .onError(error -> logAndShowErrorSnack("An error occurred while creating the activity",
                                        error,
                                        LOGGER));
                    })
                    .show();
        });

        ((ScrollViewCategory) findViewById(R.id.administrativeFilesCategories)).setOnAddAction(() -> {
            fileUploadDialog = new TargetedFileUploadDialog(this, "ADMINISTRATIVE");
            fileUploadDialog.show();
        });

        ((ScrollViewCategory<FileMetadataViewModel>) findViewById(R.id.administrativeFilesCategories))
                .setOnDeleteAction(this::showFileDeletionDialog);

        ((ScrollViewCategory) findViewById(R.id.announcementsCategories)).setOnAddAction(() -> {
            AnnouncementInputDialog announcementInputDialog = new AnnouncementInputDialog(this);
            announcementInputDialog.setPositiveButtonAction(announcement -> {
                        if (announcement.getTitle().length() == 0) {
                            showSnackBar("Enter an announcement title", 1500);
                            return;
                        }

                        if (announcement.getMessage().length() == 0) {
                            showSnackBar("Enter an announcement message", 1500);
                            return;
                        }

                        firebaseApi.getAnnouncementService()
                                .save(announcement)
                                .onSuccess(none -> showSnackBar("Announcement saved successfully", 1500))
                                .onError(error -> logAndShowErrorSnack("An error occurred",
                                        error,
                                        LOGGER));
                announcementInputDialog.dismiss();
                    }).show();
        });

        ((ScrollViewCategory<AnnouncementViewModel>) findViewById(R.id.announcementsCategories))
                .setOnDeleteAction(this::showAnnouncementDeletionDialog);

        loadAll(personId);
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.administrator_activity_main_layout);
    }

    protected AdministratorFileUploadDialog getFileUploadDialogInstance() {
        return new AdministratorFileUploadDialog(this, entityKey);
    }

    protected void deleteFile(String administratorKey, FileMetadataViewModel fileMetadata) {
        getFirebaseApi().getAdministratorService().deleteAndUnlinkFile(administratorKey, fileMetadata.getKey())
                .onSuccess(none -> showSnackBar("Successfully deleted " + fileMetadata.getFullFileName(), 1000))
                .onError(error -> logAndShowErrorSnack("An error occurred!", error, LOGGER));
    }

    protected void onDeleteTapped(Context context) {
    }

    protected AdministratorImageUploadDialog getImageUploadDialog() {
        return new AdministratorImageUploadDialog(this, personId);
    }

    protected void deleteProfileImage() {
        firebaseApi.getAdministratorService()
                .deleteImage(personId, binding.getAdministrator().getImageMetadataKey())
                .onSuccess(none -> showSnackBar("Successfully deleted your profile image", 1000))
                .onError(error -> logAndShowErrorSnack("An error occurred while deleting your profile image",
                        error,
                        LOGGER));
    }

    protected AdministratorViewModel getBindingEntity() {
        return getBinding().getAdministrator();
    }

    private AccountType getSelectedAccountType() {
        if (((RadioButton) findViewById(R.id.registrationTokenProfessorRadioButton)).isChecked()) {
            return PROFESSOR;
        }

        return ADMINISTRATOR;
    }

    private void showFileDeletionDialog(FileMetadataViewModel file) {
        new AlertDialog.Builder(this, R.style.DialogTheme)
                .setTitle("File deletion")
                .setMessage("Are you sure you wish to delete this file?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    firebaseApi.getFileMetadataService()
                            .deleteMetadataAndContent(file.getKey())
                            .onSuccess(none -> showSnackBar("Successfully deleted file!", 1500))
                            .onError(error -> logAndShowErrorSnack("An error occurred", error, LOGGER));
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void showAnnouncementDeletionDialog(AnnouncementViewModel announcement) {
        new AlertDialog.Builder(this, R.style.DialogTheme)
                .setTitle("Announcement deletion")
                .setMessage("Are you sure you wish to delete this announcement?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    firebaseApi.getAnnouncementService()
                            .deleteById(announcement.getKey())
                            .onSuccess(none -> showSnackBar("Successfully deleted announcement!", 1500))
                            .onError(error -> logAndShowErrorSnack("An error occurred", error, LOGGER));
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void saveCourses(List<Course> courses) {
        for (Course course : courses) {
            firebaseApi.getCourseService()
                    .save(course)
                    .onComplete(none -> parsingHandler.post(parsingResultRunnable));
        }
    }

    private void saveProfessors(List<Professor> professors) {
        for (Professor professor : professors) {
            firebaseApi.getProfessorService()
                    .save(professor)
                    .onComplete(none -> parsingHandler.post(parsingResultRunnable));
        }
    }

    private void saveOtherActivities(List<OtherActivity> otherActivities) {
        for (OtherActivity otherActivity : otherActivities) {
            firebaseApi.getOtherActivityService()
                    .save(otherActivity)
                    .onComplete(none -> parsingHandler.post(parsingResultRunnable));
        }
    }

    private void saveRecurringClasses(List<RecurringClass> recurringClasses) {
        for (RecurringClass recurringClass : recurringClasses) {
            firebaseApi.getRecurringClassService()
                    .save(recurringClass)
                    .onComplete(none -> parsingHandler.post(parsingResultRunnable));
        }
    }

    private void saveOneTimeClasses(List<OneTimeClass> oneTimeClasses) {
        for (OneTimeClass oneTimeClass : oneTimeClasses) {
            firebaseApi.getOneTimeClassService()
                    .save(oneTimeClass)
                    .onComplete(none -> parsingHandler.post(parsingResultRunnable));
        }
    }
}
