package com.lonn.studentassistant.activities.abstractions;

import android.content.Intent;
import android.os.Bundle;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
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
						.deleteById(fileViewModel.getKey());

				firebaseApi.getFileContentService()
						.deleteById(fileViewModel.getFileContentKey());

				removeFileMetadataFromEntity(entityKey, fileViewModel.getKey());
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fileUploadDialog.setFile(requestCode, resultCode, data);
	}

	protected abstract void removeFileMetadataFromEntity(String entityKey, String metadataKey);

	protected abstract FileUploadDialog getFileUploadDialogInstance();
}
