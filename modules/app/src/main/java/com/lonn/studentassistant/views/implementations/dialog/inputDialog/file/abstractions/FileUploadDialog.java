package com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.utils.file.CustomFileReader;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.lonn.studentassistant.utils.file.FileUtils.getMimeFromUri;
import static java.util.UUID.randomUUID;

public abstract class FileUploadDialog extends Dialog {
    private static final int SNACK_BAR_ERROR_DURATION = 1000;
    private static final Logger LOGGER = Logger.ofClass(FileUploadDialog.class);
    final int requestCode;
    protected FirebaseConnectedActivity firebaseConnectedActivity;
    protected String aggregatedEntityKey;
    FileMetadataViewModel fileMetadata;
    FileContentViewModel fileContent;
    Uri selectedFileUri;
    String fileType;
    private CustomFileReader customFileReader;

    FileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
                     String aggregatedEntityKey,
                     int requestCode,
                     String fileType) {
        super(firebaseConnectedActivity);
        this.firebaseConnectedActivity = firebaseConnectedActivity;
        this.aggregatedEntityKey = aggregatedEntityKey;
        this.requestCode = requestCode;
        this.fileType = fileType;

        customFileReader = new CustomFileReader(firebaseConnectedActivity.getContentResolver());

        fileMetadata = new FileMetadataViewModel()
                .setKey(randomUUID().toString());

        fileContent = new FileContentViewModel()
                .setFileMetadataKey(fileMetadata.getKey())
                .setKey(randomUUID().toString());

        fileMetadata.setFileContentKey(fileContent.getKey());
    }

    public void setFile(int requestCode, int resultCode, Intent data) {
        if (shouldSaveFile(requestCode, resultCode)) {
            selectedFileUri = data.getData();

            fileMetadata = fileMetadata
                    .setFileSize(customFileReader.getFileSize(selectedFileUri))
                    .setFileName(customFileReader.getFileName(selectedFileUri))
                    .setFileType(getMimeFromUri(getContext(), selectedFileUri));
        }
    }

    protected void logAndShowException(String errorMessage, Exception exception) {
        firebaseConnectedActivity.showSnackBar(errorMessage, SNACK_BAR_ERROR_DURATION);
        LOGGER.error(errorMessage, exception);
    }

    protected abstract void saveFile(String laboratoryKey, FileMetadataViewModel fileMetadata, FileContentViewModel fileContent);

    void readAndSaveFile(FileContentViewModel fileContent, FileMetadataViewModel fileMetadata) {
        firebaseConnectedActivity.showSnackBar("Uploading file");

        try {
            fileContent = fileContent
                    .setFileContentBase64(customFileReader.readBase64(selectedFileUri));

            saveFile(aggregatedEntityKey, fileMetadata, fileContent);
        } catch (IOException exception) {
            logAndShowException("An error occurred while reading the file", exception);
        }
    }

    boolean shouldSaveFile(int requestCode, int resultCode) {
        return requestCode == this.requestCode && resultCode == RESULT_OK;
    }
}
