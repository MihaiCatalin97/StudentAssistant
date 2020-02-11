package com.lonn.studentassistant.activities.implementations.administrator;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.activities.abstractions.MainActivity;
import com.lonn.studentassistant.databinding.AdministratorActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AdministratorViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.disciplines.ActivityInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.disciplines.CourseInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.AdministratorFileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.AdministratorImageUploadDialog;

import lombok.Getter;

import static android.view.View.GONE;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.ADMINISTRATOR;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.PROFESSOR;
import static com.lonn.studentassistant.validation.Regex.EMAIL_REGEX;

public class AdministratorActivity extends MainActivity<AdministratorViewModel> {
    private static final Logger LOGGER = Logger.ofClass(AdministratorActivity.class);
    protected Dispatcher<AdministratorActivity, AdministratorViewModel> dispatcher;
    @Getter
    AdministratorActivityMainLayoutBinding binding;

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
}
