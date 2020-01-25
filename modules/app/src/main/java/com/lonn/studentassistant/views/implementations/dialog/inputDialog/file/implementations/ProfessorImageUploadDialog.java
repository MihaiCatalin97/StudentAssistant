package com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.NoGUIUploadDialog;

import static com.lonn.studentassistant.activities.abstractions.FileManagingActivity.UPLOAD_IMAGE_REQUEST_CODE;

public class ProfessorImageUploadDialog extends NoGUIUploadDialog {

    public ProfessorImageUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
                                      String aggregatedEntityKey) {
        super(firebaseConnectedActivity, aggregatedEntityKey, UPLOAD_IMAGE_REQUEST_CODE,
                "image/*");
    }

    protected void saveFile(String professorKey, FileMetadataViewModel fileMetadata, FileContentViewModel fileContent) {
        firebaseConnectedActivity.getFirebaseApi()
                .getProfessorService()
                .addOrReplaceImage(professorKey, fileMetadata, fileContent)
                .onSuccess(none -> firebaseConnectedActivity.showSnackBar("Successfully uploaded " + fileMetadata.getFullFileName(), 1000))
                .onError(exception -> logAndShowException("An error occurred while uploading the image",
                        exception));
    }
}
