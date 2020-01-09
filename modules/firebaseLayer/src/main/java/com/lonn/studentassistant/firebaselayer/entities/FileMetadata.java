package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileMetadata extends BaseEntity {
    private String fileName;
    private long fileSize;
    private String fileType;
    private String fileContentKey;
    private String fileTitle;
    private String fileDescription;
}
