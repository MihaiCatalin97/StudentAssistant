package com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.utils.file.CustomFileReader;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static java.util.UUID.randomUUID;

public abstract class FileUploadDialog extends Dialog {
	private static final int SNACK_BAR_ERROR_DURATION = 1000;
	private static final Logger LOGGER = Logger.ofClass(FileUploadDialog.class);
	final int requestCode;
	protected FirebaseConnectedActivity firebaseConnectedActivity;
	FileMetadataViewModel fileMetadata;
	FileContentViewModel fileContent;
	Uri selectedFileUri;
	String fileType;
	String fileTypeName;
	private String aggregatedEntityKey;
	private CustomFileReader customFileReader;

	FileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
					 String aggregatedEntityKey,
					 int requestCode,
					 String fileType,
					 String fileTypeName) {
		super(firebaseConnectedActivity);
		this.firebaseConnectedActivity = firebaseConnectedActivity;
		this.aggregatedEntityKey = aggregatedEntityKey;
		this.requestCode = requestCode;
		this.fileType = fileType;
		this.fileTypeName = fileTypeName;

		customFileReader = new CustomFileReader(firebaseConnectedActivity.getContentResolver());

		fileMetadata = new FileMetadataViewModel()
				.setKey(randomUUID().toString());

		fileContent = new FileContentViewModel()
				.setFileMetadataKey(fileMetadata.getKey())
				.setKey(randomUUID().toString());

		fileMetadata.setFileContentKey(fileContent.getKey());
	}

	public void setFile(int requestCode, int resultCode, Intent data) {
		if (shouldSetFile(requestCode, resultCode)) {
			selectedFileUri = data.getData();

			fileMetadata = fileMetadata
					.setFileSize(customFileReader.getFileSize(selectedFileUri))
					.setFileName(customFileReader.getFileName(selectedFileUri))
					.setFileType(customFileReader.getFileType(selectedFileUri));
		}
	}

	void saveFile(FileContentViewModel fileContent, FileMetadataViewModel fileMetadata) {
		try {
			fileContent = fileContent
					.setFileContentBase64(customFileReader.readBase64(selectedFileUri));
		}
		catch (IOException exception) {
			logAndShowException("An error occurred while reading the file", exception);
		}

		firebaseConnectedActivity.getFirebaseApi()
				.getFileContentService()
				.save(fileContent)
				.onComplete(none -> saveFileMetadata(fileMetadata),
						error -> logAndShowException("An error orccured while uploading the file", error));
	}

	private void saveFileMetadata(FileMetadataViewModel fileMetadata) {
		firebaseConnectedActivity.getFirebaseApi()
				.getFileMetadataService()
				.save(fileMetadata)
				.onComplete(none -> linkFileToEntity(aggregatedEntityKey, fileMetadata),
						error -> {
							logAndShowException("An error occurred while uploading the file", error);
							deleteFileContent(fileMetadata.getFileContentKey());
						});
	}

	protected void deleteFileMetadata(String fileMetadataKey) {
		firebaseConnectedActivity.getFirebaseApi()
				.getFileMetadataService()
				.deleteById(fileMetadataKey)
				.onCompleteDoNothing();
	}

	protected void deleteFileContent(String fileContentKey) {
		firebaseConnectedActivity.getFirebaseApi()
				.getFileContentService()
				.deleteById(fileContentKey)
				.onCompleteDoNothing();
	}

	protected void logAndShowException(String errorMessage, Exception exception) {
		firebaseConnectedActivity.showSnackBar(errorMessage, SNACK_BAR_ERROR_DURATION);
		LOGGER.error(errorMessage, exception);
	}

	protected abstract void linkFileToEntity(String aggregatedEntityKey, FileMetadataViewModel fileMetadata);

	boolean shouldSetFile(int requestCode, int resultCode) {
		return requestCode == this.requestCode && resultCode == RESULT_OK;
	}
}
