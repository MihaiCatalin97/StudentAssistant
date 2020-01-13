package com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.GUIUploadDialog;

import static com.lonn.studentassistant.activities.abstractions.FileManagingActivity.UPLOAD_FILE_REQUEST_CODE;

public class ProfessorFileUploadDialog extends GUIUploadDialog {
	public ProfessorFileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
									 String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey, UPLOAD_FILE_REQUEST_CODE,
				"*/*", "image");
	}

	protected void linkFileToEntity(String entityKey, FileMetadataViewModel fileMetadata) {
		firebaseConnectedActivity.getFirebaseApi()
				.getProfessorService()
				.getById(entityKey)
				.subscribe(false)
				.onComplete(entity -> {
					entity.getFilesMetadata().add(fileMetadata.getKey());
					firebaseConnectedActivity.showSnackBar("Uploading ");
					firebaseConnectedActivity.getFirebaseApi()
							.getProfessorService()
							.save(entity)
							.onComplete(none -> firebaseConnectedActivity.showSnackBar("Successfully uploaded " + fileMetadata.getFullFileName(), 1000),
									exception -> {
										logAndShowException("An error occurred while linking the file to the professor",
												exception);

										deleteFileContent(fileMetadata.getFileContentKey());
										deleteFileMetadata(fileMetadata.getKey());
									}
							);
				});
	}
}
