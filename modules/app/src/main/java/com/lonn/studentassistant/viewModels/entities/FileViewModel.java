package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
import com.lonn.studentassistant.viewModels.entities.abstractions.EntityViewModel;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileViewModel extends EntityViewModel<FileMetadata> {
    @Bindable
    public String fileName;
    @Bindable
    public String fileTitle;
    @Bindable
    public String fileDescription;
    public long fileSize;
    @Bindable
    public String fileType;

    @Bindable
    public String getFileSize() {
        return fileSize / 1024f + " MB";
    }
}
