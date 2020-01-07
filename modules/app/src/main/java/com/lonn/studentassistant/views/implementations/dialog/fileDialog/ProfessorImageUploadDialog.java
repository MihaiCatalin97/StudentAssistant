package com.lonn.studentassistant.views.implementations.dialog.fileDialog;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public class ProfessorImageUploadDialog extends FileUploadDialog {

	public ProfessorImageUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
									  String aggregatedEntityKey) {
		super(firebaseConnectedActivity, aggregatedEntityKey);
	}

	protected void linkFileToEntity(String professorKey, FileMetadata fileMetadata) {
		firebaseConnectedActivity.getFirebaseConnection().execute(new GetRequest<Professor>()
				.databaseTable(PROFESSORS)
				.predicate(where(ID).equalTo(professorKey))
				.onSuccess((professors) -> {
					Professor professor = professors.get(0);
					professor.setProfessorImageMetadataKey(fileMetadata.getKey());

					firebaseConnectedActivity.showSnackBar("Uploading...");

					firebaseConnectedActivity.getFirebaseConnection().execute(new SaveRequest<Professor>()
							.databaseTable(PROFESSORS)
							.entity(professor)
							.onSuccess(() ->
									firebaseConnectedActivity.showSnackBar(
											"Successfully uploaded " + fileMetadata.getFullFileName(),
											1000))
							.onError((exception) -> {
								logAndShowException("An error occurred while linking the file to the professor",
										exception);

								deleteFileContent(fileMetadata.getFileContentKey());
								deleteFileMetadata(fileMetadata.getKey());
							}));
				})
				.subscribe(false));
	}

	protected boolean shouldHaveMetadata() {
		return false;
	}
}
