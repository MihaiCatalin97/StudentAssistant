package com.lonn.studentassistant.views.implementations.dialog.fileDialog;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public class CourseFileUploadDialog extends FileUploadDialog {

	public CourseFileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
								  String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey);
	}

	protected void linkFileToEntity(String courseKey, FileMetadataViewModel fileMetadata) {
		firebaseConnectedActivity.getFirebaseApi()
				.getCourseService()
				.getById(courseKey)
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

	protected boolean shouldHaveMetadata() {
		return true;
	}
}
