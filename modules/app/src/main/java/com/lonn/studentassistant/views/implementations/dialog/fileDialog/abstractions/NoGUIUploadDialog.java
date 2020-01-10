package com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions;

import android.content.Intent;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;

import static android.content.Intent.createChooser;

public abstract class NoGUIUploadDialog extends FileUploadDialog {
	public NoGUIUploadDialog(FirebaseConnectedActivity firebaseConnectedActivity,
							 String aggregatedEntityKey,
							 int requestCode,
							 String fileType,
							 String fileTypeName) {
		super(firebaseConnectedActivity, aggregatedEntityKey, requestCode, fileType, fileTypeName);
	}

	public void setFile(int requestCode, int resultCode, Intent data) {
		super.setFile(requestCode, resultCode, data);

		if (shouldSetFile(requestCode, resultCode)) {
			saveFile(fileContent, fileMetadata);
		}
	}

	@Override
	public void show() {
		Intent intent = new Intent()
				.setType(fileType)
				.setAction(Intent.ACTION_GET_CONTENT);

		firebaseConnectedActivity.startActivityForResult(createChooser(intent, "Select a " + fileTypeName),
				requestCode);
	}
}
