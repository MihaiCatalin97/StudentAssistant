package com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.NoGUIUploadDialog;

import static com.lonn.studentassistant.activities.abstractions.FileManagingActivity.UPLOAD_IMAGE_REQUEST_CODE;

public class ProfessorImageUploadDialog extends NoGUIUploadDialog {

	public ProfessorImageUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
									  String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey, UPLOAD_IMAGE_REQUEST_CODE,
				"*/*", "image");
	}

	protected void linkFileToEntity(String professorKey, FileMetadataViewModel fileMetadata) {
		firebaseConnectedActivity.getFirebaseApi()
				.getProfessorService()
				.getById(professorKey)
				.subscribe(false)
				.onComplete(professor -> {
							professor.setProfessorImageMetadataKey(fileMetadata.getKey());

							firebaseConnectedActivity.showSnackBar("Uploading...");
							firebaseConnectedActivity.getFirebaseApi()
									.getProfessorService()
									.save(professor)
									.onComplete(none -> firebaseConnectedActivity.showSnackBar(
											"Successfully uploaded " + fileMetadata.getFullFileName(),
											1000),
											exception -> {
												logAndShowException("An error occurred while linking the image to the professor",
														exception);

												deleteFileContent(fileMetadata.getFileContentKey());
												deleteFileMetadata(fileMetadata.getKey());
											});
						},
						exception -> logAndShowException("An error occurred while uploading the image",
								exception));
	}
}
