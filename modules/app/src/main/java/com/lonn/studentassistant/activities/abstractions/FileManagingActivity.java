package com.lonn.studentassistant.activities.abstractions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.views.implementations.EntityView;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.FileUploadDialog;

public abstract class FileManagingActivity<T extends EntityViewModel<? extends BaseEntity>> extends EntityActivity<T> {
	private FileUploadDialog fileUploadDialog;

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
			showDownloadDialog((FileMetadataViewModel) ((EntityView) v.getParent()).getEntityViewModel());

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
				showSnackBar("File downloaded successfully!");
				break;
			}
		}

	}

	protected abstract void removeFileMetadataFromEntity(String entityKey, String metadataKey);

	protected abstract FileUploadDialog getFileUploadDialogInstance();

	protected void handleDownload() {

	}

	private void showDownloadDialog(FileMetadataViewModel entity) {
		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("*/" + entity.getFileType());
		intent.putExtra(Intent.EXTRA_TITLE, entity.getFileName());

		startActivityForResult(intent, SAVE_DIALOG_REQUEST_CODE);
	}

	public static final int SAVE_DIALOG_REQUEST_CODE = 20000;
	public static final int UPLOAD_FILE_REQUEST_CODE = 10000;
	public static final int UPLOAD_IMAGE_REQUEST_CODE = 10001;
}
