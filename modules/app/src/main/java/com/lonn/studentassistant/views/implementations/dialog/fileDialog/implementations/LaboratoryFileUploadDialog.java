package com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.GUIUploadDialog;

import static com.lonn.studentassistant.activities.abstractions.FileManagingActivity.UPLOAD_FILE_REQUEST_CODE;

public class LaboratoryFileUploadDialog extends GUIUploadDialog {
	public LaboratoryFileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
								  String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey, UPLOAD_FILE_REQUEST_CODE,
				"*/*");
	}

	protected void linkFileToEntity(String laboratoryKey, FileMetadataViewModel fileMetadata) {
		firebaseConnectedActivity.getFirebaseApi()
				.getLaboratoryService()
				.getById(laboratoryKey)
				.subscribe(false)
				.onComplete(laboratory -> {
					laboratory.getFileMetadataKeys().add(fileMetadata.getKey());

					firebaseConnectedActivity.showSnackBar("Uploading ");
					firebaseConnectedActivity.getFirebaseApi()
							.getLaboratoryService()
							.save(laboratory)
							.onComplete(none -> firebaseConnectedActivity.showSnackBar("Successfully uploaded " + fileMetadata.getFullFileName(), 1000),
									exception -> {
										logAndShowException("An error occurred while linking the file to the course",
												exception);

										deleteFileContent(fileMetadata.getFileContentKey());
										deleteFileMetadata(fileMetadata.getKey());
									}
							);
				});
	}
}
