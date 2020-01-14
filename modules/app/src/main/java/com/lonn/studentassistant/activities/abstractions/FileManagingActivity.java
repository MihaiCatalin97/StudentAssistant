package com.lonn.studentassistant.activities.abstractions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.EntityView;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.FileUploadDialog;

import java.io.IOException;
import java.io.OutputStream;

import static android.util.Base64.DEFAULT;
import static android.util.Base64.decode;

public abstract class FileManagingActivity<T extends EntityViewModel<? extends BaseEntity>> extends EntityActivity<T> {
	private static final Logger LOGGER = Logger.ofClass(FileManagingActivity.class);
	private FileUploadDialog fileUploadDialog;
	private FileMetadataViewModel fileToDownload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ScrollViewCategory<FileMetadataViewModel> filesCategory = findViewById(R.id.filesCategory);

		if (filesCategory != null) {
			filesCategory.setOnAddAction(() -> {
				fileUploadDialog = getFileUploadDialogInstance();
				fileUploadDialog.show();
			});

			filesCategory.setOnDeleteAction((FileMetadataViewModel fileViewModel) -> {
				firebaseApi.getFileMetadataService()
						.deleteById(fileViewModel.getKey())
						.onCompleteDoNothing();

				firebaseApi.getFileContentService()
						.deleteById(fileViewModel.getFileContentKey())
						.onCompleteDoNothing();

				removeFileMetadataFromEntity(entityKey, fileViewModel.getKey());
			});
		}
	}


	public boolean tapScrollViewEntityLayout(View v) {
		if (!super.tapScrollViewEntityLayout(v)) {
			View parent = v;

			while (parent != null && !(parent instanceof EntityView)) {
				parent = (ViewGroup) parent.getParent();
			}

			if (parent != null) {
				showDownloadDialog((FileMetadataViewModel) ((EntityView) parent).getEntityViewModel());
			}

			return true;
		}

		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case UPLOAD_FILE_REQUEST_CODE: {
				fileUploadDialog.setFile(requestCode, resultCode, data);
				break;
			}
			case SAVE_DIALOG_REQUEST_CODE: {
				if (resultCode == RESULT_OK) {
					downloadFile(fileToDownload, data.getData());
				}
				break;
			}
		}
	}

	protected abstract void removeFileMetadataFromEntity(String entityKey, String metadataKey);

	protected abstract FileUploadDialog getFileUploadDialogInstance();

	private void showDownloadDialog(FileMetadataViewModel entity) {
		fileToDownload = entity;

		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType(entity.getFileType());
		intent.putExtra(Intent.EXTRA_TITLE, entity.getFullFileName());

		startActivityForResult(intent, SAVE_DIALOG_REQUEST_CODE);
	}

	private void downloadFile(final FileMetadataViewModel fileMetadata, final Uri fileLocation) {
		showSnackBar("Downloading file");
		firebaseApi.getFileContentService()
				.getById(fileMetadata.getFileContentKey())
				.onComplete(file -> {
							saveFile(decode(file.getFileContentBase64(), DEFAULT),
									fileLocation);
						},
						error -> logAndShowError("An error occurred while downloading the file",
								error,
								LOGGER));
	}

	private void saveFile(byte[] fileContent, Uri fileLocation) {
		OutputStream outputStream;

		try {
			if (fileLocation.getPath() == null) {
				throw new IOException("File not found");
			}
			outputStream = getBaseContext().getContentResolver().openOutputStream(fileLocation);

			if (outputStream != null) {
				outputStream.write(fileContent);
				outputStream.close();
				showSnackBar("File downloaded successfully!", 1000);
			}
		}
		catch (IOException exception) {
			logAndShowError("An error occurred while downloading the file",
					exception,
					LOGGER);
		}
	}

	public static final int SAVE_DIALOG_REQUEST_CODE = 20000;
	public static final int UPLOAD_FILE_REQUEST_CODE = 10000;
	public static final int UPLOAD_IMAGE_REQUEST_CODE = 10001;
}
