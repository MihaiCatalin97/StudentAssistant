package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.entities.FileContent;

public class FileContentFields<T> extends BaseEntityField<FileContent, T> {
    public static FileContentFields<String> FILE_METADATA_KEY = new FileContentFields<>("fileMetadataKey");

    private FileContentFields(String fieldName) {
        super(fieldName);
    }
}