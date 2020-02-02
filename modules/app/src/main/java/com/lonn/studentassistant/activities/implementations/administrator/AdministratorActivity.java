package com.lonn.studentassistant.activities.implementations.administrator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.activities.abstractions.MainActivity;
import com.lonn.studentassistant.databinding.AdministratorActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.viewModels.AdministratorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.ProfileImageUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.AdministratorImageUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.ProfessorFileUploadDialog;

import lombok.Getter;

import static android.view.View.GONE;
import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.ADMINISTRATOR;
import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.PROFESSOR;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidEmail;

public class AdministratorActivity extends MainActivity<AdministratorViewModel> {
	private static final Logger LOGGER = Logger.ofClass(AdministratorActivity.class);
	@Getter
	AdministratorActivityMainLayoutBinding binding;
	protected Dispatcher<AdministratorActivity, AdministratorViewModel> dispatcher;

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

		loadAll(personId);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.administrator_activity_main_layout);
	}

	public void createRegistrationToken(View view) {
		AccountType accountType = getSelectedAccountType();
		String email = ((EditText) findViewById(R.id.registrationTokenEmailEditText)).getText().toString();

		if (!isValidEmail(email)) {
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

	private AccountType getSelectedAccountType() {
		if (((RadioButton) findViewById(R.id.registrationTokenProfessorRadioButton)).isChecked()) {
			return PROFESSOR;
		}

		return ADMINISTRATOR;
	}

	public void onEditTapped() {
		boolean editing = binding.getEditingProfile() == null ? false : binding.getEditingProfile();

		binding.setEditingProfile(!editing);
	}

	public void onSaveTapped() {
		dispatcher.update(binding.getAdministrator());
	}

	protected void onDiscardTapped() {
		binding.setAdministrator(dispatcher.getCurrentProfile());
		binding.setEditingProfile(false);
	}

	protected ProfessorFileUploadDialog getFileUploadDialogInstance() {
		return new ProfessorFileUploadDialog(this, entityKey);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		imageUploadDialog.setFile(requestCode, resultCode, data);
	}

	protected void deleteFile(String professorKey, FileMetadataViewModel fileMetadata) {
		getFirebaseApi().getProfessorService().deleteAndUnlinkFile(professorKey, fileMetadata.getKey())
				.onSuccess(none -> showSnackBar("Successfully deleted " + fileMetadata.getFullFileName()))
				.onError(error -> logAndShowErrorSnack("An error occured!", error, LOGGER));
	}

	protected void onDeleteTapped(Context context) {
	}

	protected ProfileImageUploadDialog getImageUploadDialog() {
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
}
