package com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.createChooser;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.lonn.studentassistant.utils.file.FileUtils.getFileTypeFromMime;

public abstract class GUIUploadDialog extends FileUploadDialog {
	private EditText fileTitleEditText;
	private EditText fileDescriptionEditText;
	private TextView fileNameText;

	public GUIUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
						   String aggregatedEntityKey,
						   int requestCode,
						   String fileType) {
		super(firebaseConnectedActivity, aggregatedEntityKey, requestCode, fileType);
	}

	@Override
	public void show() {
		super.show();

		if (getWindow() != null) {
			int desiredWidth = firebaseConnectedActivity.getResources().getDisplayMetrics().widthPixels * 9 / 10;
			getWindow().setLayout(desiredWidth, WRAP_CONTENT);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.file_input_dialog);

		findViewById(R.id.fileInputDialogChooseButton).setOnClickListener((v) -> {
			Intent intent = new Intent()
					.setType(fileType)
					.setAction(Intent.ACTION_GET_CONTENT);

			firebaseConnectedActivity.startActivityForResult(createChooser(intent, "Select a " + getFileTypeFromMime(fileType)),
					requestCode);
		});

		findViewById(R.id.fileInputDialogNegativeButton).setOnClickListener((v) -> dismiss());

		findViewById(R.id.fileInputDialogPositiveButton).setOnClickListener((v) -> readAndUpload());

		init();
	}

	private void init() {
		fileTitleEditText = findViewById(R.id.fileInputDialogFileTitle);
		fileDescriptionEditText = findViewById(R.id.fileInputDialogFileDescription);
		fileNameText = findViewById(R.id.fileInputDialogChosenFileName);
	}

	@Override
	public void setFile(int requestCode, int resultCode, Intent data) {
		super.setFile(requestCode, resultCode, data);

		if (requestCode == this.requestCode && resultCode == RESULT_OK) {
			if (fileNameText != null) {
				fileNameText.setText(fileMetadata.getFullFileName());
			}
		}
	}

	private void readAndUpload() {
		if (fileDescriptionEditText != null) {
			fileMetadata.setFileDescription(fileDescriptionEditText.getText().toString());
		}

		if (fileTitleEditText != null) {
			fileMetadata.setFileTitle(fileTitleEditText.getText().toString());
		}

		if (fileMetadata.getFileTitle() == null || fileMetadata.getFileTitle().length() == 0) {
			Toast.makeText(getContext(), "Please enter a file title", Toast.LENGTH_SHORT).show();
		}
		else if (selectedFileUri == null) {
			Toast.makeText(getContext(), "Please select a file", Toast.LENGTH_SHORT).show();
		}
		else {
			readAndSaveFile(fileContent, fileMetadata);
			dismiss();
		}
	}
}
