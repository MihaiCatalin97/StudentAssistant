package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public abstract class FileAssociatedEntityViewModel<T extends BaseEntity> extends EntityViewModel<T> {
    protected List<String> fileMetadataKeys = new ArrayList<>();
}
