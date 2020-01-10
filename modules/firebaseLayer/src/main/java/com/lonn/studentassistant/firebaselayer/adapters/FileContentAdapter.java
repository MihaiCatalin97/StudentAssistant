package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;

import static com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel.builder;

public class FileContentAdapter extends ViewModelAdapter<FileContent, FileContentViewModel> {
	public FileContentViewModel adapt(FileContent fileContent) {
		return builder()
				.fileContentBase64(fileContent.getFileContentBase64())
				.fileMetadataKey(fileContent.getFileMetadataKey())
				.build()
				.setKey(fileContent.getKey());
	}

	public FileContent adapt(FileContentViewModel fileContentViewModel) {
		FileContent result = new FileContent()
				.setFileContentBase64(fileContentViewModel.getFileContentBase64())
				.setFileMetadataKey(fileContentViewModel.getFileMetadataKey());

		if (fileContentViewModel.getKey() != null) {
			result.setKey(fileContentViewModel.getKey());
		}

		return result;
	}
}