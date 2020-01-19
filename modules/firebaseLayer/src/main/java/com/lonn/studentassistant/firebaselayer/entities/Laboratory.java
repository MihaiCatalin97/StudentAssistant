package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.FileAssociatedEntity;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class Laboratory extends FileAssociatedEntity {
    private String course;
    private List<String> grades = new LinkedList<>();
    private String description;
    private String title;
    private int weekNumber;

    public Laboratory setFileMetadataKeys(List<String> fileMetadataKeys) {
        this.fileMetadataKeys = fileMetadataKeys;
        return this;
    }
}
