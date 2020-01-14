package com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.GUIUploadDialog;

import static com.lonn.studentassistant.activities.abstractions.FileManagingActivity.UPLOAD_FILE_REQUEST_CODE;

public class CourseFileUploadDialog extends GUIUploadDialog {
	public CourseFileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
								  String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey, UPLOAD_FILE_REQUEST_CODE,
				"*/*");
	}

	protected void linkFileToEntity(String courseKey, FileMetadataViewModel fileMetadata) {
		firebaseConnectedActivity.getFirebaseApi()
				.getCourseService()
				.getById(courseKey)
				.subscribe(false)
				.onComplete(course -> {
					course.getFilesMetadata().add(fileMetadata.getKey());
					firebaseConnectedActivity.showSnackBar("Uploading ");
					firebaseConnectedActivity.getFirebaseApi()
							.getCourseService()
							.save(course)
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
