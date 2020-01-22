package com.lonn.studentassistant.activities.implementations.entityActivities.professor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FileManagingActivity;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations.ProfessorFileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations.ProfessorImageUploadDialog;

import lombok.Getter;

public class ProfessorEntityActivity extends FileManagingActivity<ProfessorViewModel> {
	private static final Logger LOGGER = Logger.ofClass(ProfessorEntityActivity.class);
	@Getter
	ProfessorEntityActivityLayoutBinding binding;
	private ProfessorEntityActivityFirebaseDispatcher dispatcher;
	private ProfessorImageUploadDialog imageUploadDialog;

	protected void loadAll(String entityKey) {
		dispatcher.loadAll(entityKey);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.professor_entity_activity_layout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dispatcher = new ProfessorEntityActivityFirebaseDispatcher(this);
		imageUploadDialog = new ProfessorImageUploadDialog(this, entityKey);

		findViewById(R.id.imageUploadButton).setOnClickListener((v) -> {
			imageUploadDialog.show();
		});

		loadAll(entityKey);
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

	protected ProfessorFileUploadDialog getFileUploadDialogInstance() {
		return new ProfessorFileUploadDialog(this, entityKey);
	}

	@Override
	protected void onEditTapped() {
		binding.setEditing(true);
	}

	@Override
	protected void onDeleteTapped(Context context) {
		new AlertDialog.Builder(context, R.style.DialogTheme)
				.setTitle("Confirm deletion")
				.setMessage("Are you sure you want to delete this professor?")
				.setNegativeButton("Cancel", null)
				.setPositiveButton("Delete", (dialog, which) -> dispatcher.delete(binding.getEntity()))
				.create()
				.show();
	}

	@Override
	protected void onSaveTapped() {
		dispatcher.update(binding.getEntity());
	}
}
