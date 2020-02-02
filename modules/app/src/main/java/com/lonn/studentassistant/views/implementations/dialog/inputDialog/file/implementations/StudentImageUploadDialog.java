package com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.ProfileImageUploadDialog;

public class StudentImageUploadDialog extends ProfileImageUploadDialog {

	public StudentImageUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
									  String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey);
	}

	protected void saveFile(String professorKey, FileMetadataViewModel fileMetadata, FileContentViewModel fileContent) {
		firebaseConnectedActivity.getFirebaseApi()
				.getStudentService()
				.addOrReplaceImage(professorKey, fileMetadata, fileContent)
				.onSuccess(none -> firebaseConnectedActivity.showSnackBar("Successfully uploaded " + fileMetadata.getFullFileName(), 1000))
				.onError(exception -> logAndShowException("An error occurred while uploading the image",
						exception));
	}
}
