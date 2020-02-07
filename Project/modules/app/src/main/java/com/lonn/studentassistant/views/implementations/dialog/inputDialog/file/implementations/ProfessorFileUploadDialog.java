package com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.GUIUploadDialog;

import static com.lonn.studentassistant.activities.abstractions.FileManagingActivity.UPLOAD_FILE_REQUEST_CODE;

public class ProfessorFileUploadDialog extends GUIUploadDialog {
	public ProfessorFileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
									 String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey, UPLOAD_FILE_REQUEST_CODE,
				"*/*");
	}

	protected void saveFile(String professorKey, FileMetadataViewModel fileMetadata, FileContentViewModel fileContent) {
		firebaseConnectedActivity.getFirebaseApi()
				.getProfessorService()
				.createAndLinkFile(professorKey, fileMetadata, fileContent)
				.onSuccess(none -> firebaseConnectedActivity.showSnackBar("Successfully uploaded " + fileMetadata.getFullFileName(), 1000))
				.onError(exception -> logAndShowException("An error occurred while uploading the file",
						exception));
	}
}
