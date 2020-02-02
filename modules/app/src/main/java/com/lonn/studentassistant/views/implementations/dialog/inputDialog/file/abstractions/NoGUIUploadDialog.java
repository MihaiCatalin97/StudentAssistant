package com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions;

import android.content.Intent;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;

import static android.content.Intent.createChooser;
import static com.lonn.studentassistant.utils.file.FileUtils.getFileTypeFromMime;

public abstract class NoGUIUploadDialog extends FileUploadDialog {
    public NoGUIUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
                             String aggregatedEntityKey,
                             int requestCode,
                             String fileType) {
        super(firebaseConnectedActivity, aggregatedEntityKey, requestCode, fileType);
    }

    public void setFile(int requestCode, int resultCode, Intent data) {
        super.setFile(requestCode, resultCode, data);

        if (shouldSaveFile(requestCode, resultCode)) {
            readAndSaveFile(fileContent, fileMetadata);
        }
    }

    @Override
    public void show() {
        Intent intent = new Intent()
                .setType(fileType)
                .setAction(Intent.ACTION_GET_CONTENT);

        firebaseConnectedActivity.startActivityForResult(createChooser(intent, "Select a " + getFileTypeFromMime(fileType)),
                requestCode);
    }
}
