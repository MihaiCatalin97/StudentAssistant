package com.lonn.studentassistant.views.implementations.dialog.fileDialog;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.requests.DeleteByIdRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.utils.file.CustomFileReader;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.createChooser;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_CONTENT;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_METADATA;

public abstract class FileUploadDialog extends Dialog {
	public static final int SELECT_FILE_REQUEST_CODE = 123;
	private static final int SNACK_BAR_ERROR_DURATION = 1000;
	private static final Logger LOGGER = Logger.ofClass(FileUploadDialog.class);
	protected FirebaseConnectedActivity firebaseConnectedActivity;
	private String aggregatedEntityKey;
	private FileMetadata fileMetadata;
	private FileContent fileContent;
	private EditText fileTitleEditText;
	private EditText fileDescriptionEditText;
	private TextView fileNameText;
	private Uri selectedFileUri;
	private CustomFileReader customFileReader;

	public FileUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
							String aggregatedEntityKey) {
		super(firebaseConnectedActivity);
		this.firebaseConnectedActivity = firebaseConnectedActivity;
		this.aggregatedEntityKey = aggregatedEntityKey;
		customFileReader = new CustomFileReader(firebaseConnectedActivity.getContentResolver());

		fileMetadata = new FileMetadata();

		fileContent = new FileContent()
				.setFileMetadataKey(fileMetadata.getKey());

		fileMetadata.setFileContentKey(fileContent.getKey());
	}

	@Override
	public void show() {
		super.show();

		if (getWindow() != null) {
			int desiredWidth = firebaseConnectedActivity.getResources().getDisplayMetrics().widthPixels * 9 / 10;
			int desiredHeight = firebaseConnectedActivity.getResources().getDisplayMetrics().heightPixels / 2;
			getWindow().setLayout(desiredWidth, desiredHeight);
		}
	}

	public void setFile(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
			selectedFileUri = data.getData();

			fileMetadata = fileMetadata
					.setFileSize(customFileReader.getFileSize(selectedFileUri))
					.setFileName(customFileReader.getFileName(selectedFileUri))
					.setFileType(customFileReader.getFileType(selectedFileUri));

			if (fileNameText != null) {
				fileNameText.setText(fileMetadata.getFullFileName());
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.file_input_dialog);

		findViewById(R.id.fileInputDialogChooseButton).setOnClickListener((v) -> {
			Intent intent = new Intent()
					.setType("*/*")
					.setAction(Intent.ACTION_GET_CONTENT);

			firebaseConnectedActivity.startActivityForResult(createChooser(intent, "Select a file"),
					SELECT_FILE_REQUEST_CODE);
		});

		findViewById(R.id.fileInputDialogNegativeButton).setOnClickListener((v) -> dismiss());

		findViewById(R.id.fileInputDialogPositiveButton).setOnClickListener((v) -> readAndUpload());

		init();
	}

	public void readAndUpload() {
		if (fileDescriptionEditText != null) {
			fileMetadata.setFileDescription(fileDescriptionEditText.getText().toString());
		}

		if (fileTitleEditText != null) {
			fileMetadata.setFileTitle(fileTitleEditText.getText().toString());
		}

		if (shouldHaveMetadata() && (fileMetadata.getFileTitle() == null ||
				fileMetadata.getFileTitle().length() == 0)) {
			Toast.makeText(getContext(), "Please enter a file title", Toast.LENGTH_SHORT).show();
		}
		else if (selectedFileUri == null) {
			Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
		}
		else {
			saveFile(fileContent, fileMetadata);
			dismiss();
		}
	}

	private void init() {
		fileTitleEditText = findViewById(R.id.fileInputDialogFileTitle);
		fileDescriptionEditText = findViewById(R.id.fileInputDialogFileDescription);
		fileNameText = findViewById(R.id.fileInputDialogChosenFileName);
	}

	private void saveFile(FileContent fileContent, FileMetadata fileMetadata) {
		try {
			fileContent = fileContent
					.setFileContentBase64(customFileReader.readBase64(selectedFileUri));
		}
		catch (IOException exception) {
			logAndShowException("An error occurred while reading the file", exception);
		}

		firebaseConnectedActivity.getFirebaseConnection().execute(new SaveRequest<FileContent>()
				.entity(fileContent)
				.databaseTable(FILE_CONTENT)
				.onSuccess(() -> saveFileMetadata(fileMetadata))
				.onError((error) ->
						logAndShowException("An error occurred while uploading the file", error)
				));
	}

	private void saveFileMetadata(FileMetadata fileMetadata) {
		firebaseConnectedActivity.getFirebaseConnection().execute(new SaveRequest<FileMetadata>()
				.entity(fileMetadata)
				.databaseTable(FILE_METADATA)
				.onSuccess(() -> linkFileToEntity(aggregatedEntityKey, fileMetadata))
				.onError((error) -> {
					logAndShowException("An error occurred while uploading the file", error);
					deleteFileContent(fileMetadata.getFileContentKey());
				}));
	}

	protected void deleteFileMetadata(String fileMetadataKey) {
		firebaseConnectedActivity.getFirebaseConnection().execute(new DeleteByIdRequest()
				.databaseTable(FILE_METADATA)
				.key(fileMetadataKey));
	}

	protected void deleteFileContent(String fileContentKey) {
		firebaseConnectedActivity.getFirebaseConnection().execute(new DeleteByIdRequest()
				.databaseTable(FILE_CONTENT)
				.key(fileContentKey));
	}

	protected void logAndShowException(String errorMessage, Exception exception) {
		firebaseConnectedActivity.showSnackBar(errorMessage, SNACK_BAR_ERROR_DURATION);
		LOGGER.error(errorMessage, exception);
	}

	protected abstract void linkFileToEntity(String aggregatedEntityKey, FileMetadata fileMetadata);

	protected abstract boolean shouldHaveMetadata();
}
