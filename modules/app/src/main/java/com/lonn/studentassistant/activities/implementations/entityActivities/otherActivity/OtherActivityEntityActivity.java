package com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FileManagingActivity;
import com.lonn.studentassistant.databinding.OtherActivityEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.FileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations.CourseFileUploadDialog;

public class OtherActivityEntityActivity extends FileManagingActivity<OtherActivityViewModel> {
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
	}

	protected void removeFileMetadataFromEntity(String entityKey, String fileMetadataKey) {
		getFirebaseApi().getOtherActivityService()
				.getById(entityKey)
				.onComplete(otherActivityViewModel -> {
					otherActivityViewModel.getFilesMetadata().remove(fileMetadataKey);

					getFirebaseApi().getOtherActivityService()
							.save(otherActivityViewModel);
				});
	}

	protected FileUploadDialog getFileUploadDialogInstance() {
		return new CourseFileUploadDialog(this, entityKey);
	}
}
