package com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.FileMetadata;

public class FileMetadataFields<T> extends BaseEntityField<FileMetadata, T> {
    public static FileMetadataFields<String> FILE_ASSOCIATED_ENTITY_KEY = new FileMetadataFields<>("associatedEntityKey");

    private FileMetadataFields(String fieldName) {
        super(fieldName);
    }
}