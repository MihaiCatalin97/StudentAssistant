package com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.NoGUIUploadDialog;

import static com.lonn.studentassistant.activities.abstractions.FileManagingActivity.UPLOAD_IMAGE_REQUEST_CODE;

public class ProfileImageUploadDialog extends NoGUIUploadDialog {

    public ProfileImageUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
                                    String aggregatedEntityKey) {
        super(firebaseConnectedActivity, aggregatedEntityKey, UPLOAD_IMAGE_REQUEST_CODE,
                "image/*");
    }

    protected void saveFile(String studentKey, FileMetadataViewModel fileMetadata, FileContentViewModel fileContent) {
        firebaseConnectedActivity.getFirebaseApi()
                .getStudentService()
                .addOrReplaceImage(studentKey, fileMetadata, fileContent)
                .onSuccess(none -> firebaseConnectedActivity.showSnackBar("Successfully uploaded " + fileMetadata.getFullFileName(), 1000))
                .onError(exception -> logAndShowException("An error occurred while uploading the image",
                        exception));
    }
}
