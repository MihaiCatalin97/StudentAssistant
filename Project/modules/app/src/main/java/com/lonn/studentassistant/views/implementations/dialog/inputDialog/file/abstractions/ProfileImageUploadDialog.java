package com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;

import static com.lonn.studentassistant.activities.abstractions.FileManagingActivity.UPLOAD_IMAGE_REQUEST_CODE;

public abstract class ProfileImageUploadDialog extends NoGUIUploadDialog {

	public ProfileImageUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
									String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey, UPLOAD_IMAGE_REQUEST_CODE,
				"image/*");
	}

	protected abstract void saveFile(String personKey, FileMetadataViewModel fileMetadata, FileContentViewModel fileContent);
}
