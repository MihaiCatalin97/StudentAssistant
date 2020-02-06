package com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.ProfileImageUploadDialog;

public class ProfessorImageUploadDialog extends ProfileImageUploadDialog {

    public ProfessorImageUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
                                      String aggregatedEntityKey) {
        super(firebaseConnectedActivity, aggregatedEntityKey);
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
