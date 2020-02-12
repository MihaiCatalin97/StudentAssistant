package com.lonn.studentassistant.firebaselayer.businessLayer.adapters;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.FileMetadata;

import static com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel.builder;

public class FileMetadataAdapter extends ViewModelAdapter<FileMetadata, FileMetadataViewModel> {
    public FileMetadataViewModel adapt(FileMetadata fileMetadata) {
        return builder().fileDescription(fileMetadata.getFileDescription())
                .fileName(fileMetadata.getFileName())
                .fileSize(fileMetadata.getFileSize())
                .fileTitle(fileMetadata.getFileTitle())
                .fileType(fileMetadata.getFileType())
                .fileContentKey(fileMetadata.getFileContentKey())
                .associatedEntityKey(fileMetadata.getAssociatedEntityKey())
                .targetedGroups(fileMetadata.getTargetedGroups())
                .build()
                .setKey(fileMetadata.getKey());
    }

    public FileMetadata adapt(FileMetadataViewModel fileMetadataViewModel) {
        FileMetadata result = new FileMetadata()
                .setFileDescription(fileMetadataViewModel.getFileDescription())
                .setFileName(fileMetadataViewModel.getFileName())
                .setFileSize(fileMetadataViewModel.getFileSizeLong())
                .setFileTitle(fileMetadataViewModel.getFileTitle())
                .setFileType(fileMetadataViewModel.getFileType())
                .setAssociatedEntityKey(fileMetadataViewModel.getAssociatedEntityKey())
                .setFileContentKey(fileMetadataViewModel.getFileContentKey())
                .setTargetedGroups(fileMetadataViewModel.getTargetedGroups());

        if (fileMetadataViewModel.getKey() != null) {
            result.setKey(fileMetadataViewModel.getKey());
        }

        return result;
    }
}
