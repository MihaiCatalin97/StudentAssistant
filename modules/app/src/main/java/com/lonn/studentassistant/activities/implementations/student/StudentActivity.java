package com.lonn.studentassistant.activities.implementations.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.activities.abstractions.MainActivity;
import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.ProfileImageUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.ProfessorFileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.StudentImageUploadDialog;

import lombok.Getter;

import static android.view.View.GONE;

public class StudentActivity extends MainActivity<StudentViewModel> {
	private static final Logger LOGGER = Logger.ofClass(StudentActivity.class);
	@Getter
	StudentActivityMainLayoutBinding binding;
	protected Dispatcher<StudentActivity, StudentViewModel> dispatcher;

	protected void loadAll(String entityKey) {
		dispatcher.loadAll(entityKey);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dispatcher = new StudentActivityFirebaseDispatcher(this);

		findViewById(R.id.fabEdit).setOnClickListener(view -> onEditTapped());
		findViewById(R.id.fabSaveChanges).setOnClickListener(view -> onSaveTapped());
		findViewById(R.id.fabDiscardChanges).setOnClickListener(view -> onDiscardTapped());
		findViewById(R.id.fabDelete).setVisibility(GONE);

		dispatcher.loadAll(personId);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.student_activity_main_layout);
	}

	public void onEditTapped() {
		boolean editing = binding.getEditingProfile() == null ? false : binding.getEditingProfile();

		binding.setEditingProfile(!editing);
	}

	public void onSaveTapped() {
		dispatcher.update(binding.getStudent());
		binding.setEditingProfile(false);
	}

	protected void onDiscardTapped() {
		binding.setStudent(dispatcher.getCurrentProfile());
		binding.setEditingProfile(false);
	}

	protected ProfessorFileUploadDialog getFileUploadDialogInstance() {
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void deleteFile(String professorKey, FileMetadataViewModel fileMetadata) {
	}

	protected void onDeleteTapped(Context context) {
	}

	protected ProfileImageUploadDialog getImageUploadDialog() {
		return new StudentImageUploadDialog(this, personId);
	}

	protected void deleteProfileImage() {
		firebaseApi.getStudentService()
				.deleteImage(personId, binding.getStudent().getImageMetadataKey())
				.onSuccess(none -> showSnackBar("Successfully deleted your profile image", 1000))
				.onError(error -> logAndShowErrorSnack("An error occurred while deleting your profile image",
						error,
						LOGGER));
	}
}
