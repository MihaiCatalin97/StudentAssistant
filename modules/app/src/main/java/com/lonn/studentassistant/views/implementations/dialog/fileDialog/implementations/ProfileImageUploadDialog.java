package com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.NoGUIUploadDialog;

import static com.lonn.studentassistant.activities.abstractions.FileManagingActivity.UPLOAD_IMAGE_REQUEST_CODE;

public class ProfileImageUploadDialog extends NoGUIUploadDialog {

	public ProfileImageUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
									String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey, UPLOAD_IMAGE_REQUEST_CODE,
				"image/*");
	}

	protected void linkFileToEntity(String studentKey, FileMetadataViewModel fileMetadata) {
		firebaseConnectedActivity.getFirebaseApi()
				.getStudentService()
				.getById(studentKey)
				.subscribe(false)
				.onComplete(student -> {
							firebaseConnectedActivity.getFirebaseApi()
									.getFileMetadataService()
									.getById(student.getImageMetadataKey())
									.subscribe(false)
									.onComplete(metadata -> {
										firebaseConnectedActivity.getFirebaseApi()
												.getFileContentService()
												.deleteById(metadata.getFileContentKey())
												.onCompleteDoNothing();

										firebaseConnectedActivity.getFirebaseApi()
												.getFileMetadataService()
												.deleteById(metadata.getKey())
												.onCompleteDoNothing();
									});

							student.setImageMetadataKey(fileMetadata.getKey());

							firebaseConnectedActivity.showSnackBar("Uploading...");
							firebaseConnectedActivity.getFirebaseApi()
									.getStudentService()
									.save(student)
									.onComplete(none -> firebaseConnectedActivity.showSnackBar(
											"Successfully uploaded " + fileMetadata.getFullFileName(),
											1000),
											exception -> {
												logAndShowException("An error occurred while linking the image to the student",
														exception);

												deleteFileContent(fileMetadata.getFileContentKey());
												deleteFileMetadata(fileMetadata.getKey());
											});
						},
						exception -> logAndShowException("An error occurred while uploading the image",
								exception));
	}
}
