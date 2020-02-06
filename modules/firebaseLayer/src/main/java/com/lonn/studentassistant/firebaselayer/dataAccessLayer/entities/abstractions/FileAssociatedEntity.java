package com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public abstract class FileAssociatedEntity extends BaseEntity {
    @Getter
    protected List<String> fileMetadataKeys = new LinkedList<>();
}
