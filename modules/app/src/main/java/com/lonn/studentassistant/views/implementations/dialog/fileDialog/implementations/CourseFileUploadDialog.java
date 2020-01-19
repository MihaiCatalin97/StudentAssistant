package com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.GUIUploadDialog;

import static com.lonn.studentassistant.activities.abstractions.FileManagingActivity.UPLOAD_FILE_REQUEST_CODE;

public class CourseFileUploadDialog extends GUIUploadDialog {
    public CourseFileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
                                  String aggregatedEntityKey) {
        super(firebaseConnectedActivity, aggregatedEntityKey, UPLOAD_FILE_REQUEST_CODE,
                "*/*");
    }

    protected void saveFile(String courseKey, FileMetadataViewModel fileMetadata, FileContentViewModel fileContent) {
        firebaseConnectedActivity.getFirebaseApi()
                .getCourseService()
                .createAndLinkFile(courseKey, fileMetadata, fileContent)
                .onSuccess(none -> firebaseConnectedActivity.showSnackBar("Successfully uploaded " + fileMetadata.getFullFileName(), 1000))
                .onError(exception -> logAndShowException("An error occurred while uploading the file",
                        exception));
    }
}
