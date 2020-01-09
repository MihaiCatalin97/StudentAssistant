package com.lonn.studentassistant.views.implementations.dialog.fileDialog;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;

public class ProfessorImageUploadDialog extends FileUploadDialog {

	public ProfessorImageUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
									  String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey);
	}

	protected void linkFileToEntity(String professorKey, FileMetadataViewModel fileMetadata) {
		firebaseConnectedActivity.getFirebaseApi()
				.getProfessorService()
				.getById(professorKey)
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
												logAndShowException("An error occurred while linking the file to the professor",
														exception);

												deleteFileContent(fileMetadata.getFileContentKey());
												deleteFileMetadata(fileMetadata.getKey());
											});
						},
						exception -> logAndShowException("An error occurred while uploading the image",
								exception));
	}

	protected boolean shouldHaveMetadata() {
		return false;
	}
}
