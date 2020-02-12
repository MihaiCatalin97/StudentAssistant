package com.lonn.studentassistant.firebaselayer.businessLayer.adapters;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileContentViewModel;

import static com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileContentViewModel.builder;

public class FileContentAdapter extends ViewModelAdapter<FileContent, FileContentViewModel> {
	public FileContentViewModel adapt(FileContent fileContent) {
		return builder()
				.fileContentBase64(fileContent.getFileContentBase64())
				.fileMetadataKey(fileContent.getFileMetadataKey())
				.associatedEntityKey(fileContent.getAssociatedEntityKey())
				.targetedGroups(fileContent.getTargetedGroups())
				.build()
				.setKey(fileContent.getKey());
	}

	public FileContent adapt(FileContentViewModel fileContentViewModel) {
		FileContent result = new FileContent()
				.setFileContentBase64(fileContentViewModel.getFileContentBase64())
				.setFileMetadataKey(fileContentViewModel.getFileMetadataKey())
				.setAssociatedEntityKey(fileContentViewModel.getAssociatedEntityKey())
				.setTargetedGroups(fileContentViewModel.getTargetedGroups());

		if (fileContentViewModel.getKey() != null) {
			result.setKey(fileContentViewModel.getKey());
		}

		return result;
	}
}