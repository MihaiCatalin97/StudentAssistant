package com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations;


import android.widget.CheckBox;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.GUIUploadDialog;

import java.util.LinkedList;
import java.util.List;

import static com.lonn.studentassistant.activities.abstractions.FileManagingActivity.UPLOAD_FILE_REQUEST_CODE;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.PROFESSOR;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.STUDENT;

public class TargetedFileUploadDialog extends GUIUploadDialog {
    public TargetedFileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
                                    String aggregatedEntityKey) {
        super(firebaseConnectedActivity, aggregatedEntityKey, UPLOAD_FILE_REQUEST_CODE,
                "*/*");
    }

    protected void saveFile(String aggregatedEntityKey, FileMetadataViewModel fileMetadata, FileContentViewModel fileContent) {
        List<AccountType> targetedGroups = new LinkedList<>();

        if (((CheckBox) findViewById(R.id.fileInputDialogStudentTarget)).isChecked()) {
            targetedGroups.add(STUDENT);
        }
        if (((CheckBox) findViewById(R.id.fileInputDialogProfessorTarget)).isChecked()) {
            targetedGroups.add(PROFESSOR);
        }

        fileMetadata.setAssociatedEntityKey(aggregatedEntityKey);
        fileContent.setAssociatedEntityKey(aggregatedEntityKey);

        fileMetadata.setTargetedGroups(targetedGroups);
        fileContent.setTargetedGroups(targetedGroups);

        firebaseConnectedActivity.getFirebaseApi()
                .getFileMetadataService()
                .saveMetadataAndContent(fileMetadata, fileContent)
                .onSuccess(none -> firebaseConnectedActivity.showSnackBar("Successfully uploaded " + fileMetadata.getFullFileName(), 1000))
                .onError(exception -> logAndShowException("An error occurred while uploading the file",
                        exception));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.targeted_file_input_dialog;
    }
}