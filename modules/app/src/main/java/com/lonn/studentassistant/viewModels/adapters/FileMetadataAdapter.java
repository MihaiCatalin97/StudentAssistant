package com.lonn.studentassistant.viewModels.adapters;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
import com.lonn.studentassistant.viewModels.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.viewModels.entities.FileViewModel;

public class FileMetadataAdapter extends ViewModelAdapter<FileMetadata, FileViewModel> {

	public FileMetadataAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
		super(firebaseConnectedActivity);
	}

	public FileViewModel adaptOne(FileMetadata fileMetadata) {
		return (FileViewModel) FileViewModel.builder()
				.fileDescription(fileMetadata.getFileDescription())
				.fileName(fileMetadata.getFileName())
				.fileSize(fileMetadata.getFileSize())
				.fileTitle(fileMetadata.getFileTitle())
				.fileType(fileMetadata.getFileType())
				.fileContentKey(fileMetadata.getFileContentKey())
				.build()
				.setKey(fileMetadata.getKey());
	}

	protected FileViewModel resolveLinks(FileViewModel fileViewModel, FileMetadata fileMetadata) {
		return fileViewModel;
	}
}
