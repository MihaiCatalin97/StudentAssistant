package com.lonn.studentassistant.views.implementations.dialog.fileDialog;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public class CourseFileUploadDialog extends FileUploadDialog {

	public CourseFileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
								  String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey);
	}

	protected void linkFileToEntity(String courseKey, FileMetadata fileMetadata) {
		firebaseConnectedActivity.getFirebaseConnection().execute(new GetRequest<Course>()
				.databaseTable(COURSES)
				.predicate(where(ID).equalTo(courseKey))
				.onSuccess((courses) -> {
					Course course = courses.get(0);
					course.getFilesMetadata().add(fileMetadata.getKey());

					firebaseConnectedActivity.showSnackBar("Uploading ");

					firebaseConnectedActivity.getFirebaseConnection().execute(new SaveRequest<Course>()
							.databaseTable(COURSES)
							.entity(course)
							.onSuccess(() ->
									firebaseConnectedActivity.showSnackBar(
											"Successfully uploaded " + fileMetadata.getFullFileName(),
											1000))
							.onError((exception) -> {
								logAndShowException("An error occurred while linking the file to the course",
										exception);

								deleteFileContent(fileMetadata.getFileContentKey());
								deleteFileMetadata(fileMetadata.getKey());
							}));
				})
				.subscribe(false));
	}

	protected boolean shouldHaveMetadata(){
		return true;
	}
}
