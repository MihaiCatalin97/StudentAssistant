package com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FileManagingActivity;
import com.lonn.studentassistant.databinding.OtherActivityEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.FileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations.CourseFileUploadDialog;

import lombok.Getter;

public class OtherActivityEntityActivity extends FileManagingActivity<OtherActivityViewModel> {
	private static final Logger LOGGER = Logger.ofClass(OtherActivityEntityActivity.class);
	@Getter
	OtherActivityEntityActivityLayoutBinding binding;
	private OtherActivityFirebaseDispatcher dispatcher;

	protected void loadAll(String entityKey) {
		dispatcher.loadAll(entityKey);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.other_activity_entity_activity_layout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dispatcher = new OtherActivityFirebaseDispatcher(this);
		loadAll(entityKey);
	}

	protected void deleteFile(String activityKey, String fileMetadataKey) {
		getFirebaseApi().getOtherActivityService().deleteAndUnlinkFile(activityKey, fileMetadataKey)
				.onError(error -> logAndShowErrorSnack("An error occurred while deleting the file", error, LOGGER));
	}

	protected FileUploadDialog getFileUploadDialogInstance() {
		return new CourseFileUploadDialog(this, entityKey);
	}

	@Override
	protected void onEditTapped() {
		boolean editing = binding.getEditing() == null ? false : binding.getEditing();

		binding.setEditing(!editing);
	}

	@Override
	protected void onDeleteTapped(Context context) {
		new AlertDialog.Builder(context, R.style.DialogTheme)
				.setTitle("Confirm deletion")
				.setMessage("Are you sure you want to delete this activity?")
				.setNegativeButton("Cancel", null)
				.setPositiveButton("Delete", (dialog, which) -> dispatcher.delete(entityKey))
				.create()
				.show();
	}

	@Override
	protected void onSaveTapped() {
		dispatcher.update(binding.getEntity());
	}
}
