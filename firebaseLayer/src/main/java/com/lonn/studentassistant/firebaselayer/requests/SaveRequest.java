package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.interfaces.OnErrorCallback;
import com.lonn.studentassistant.firebaselayer.models.BaseEntity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SaveRequest<T extends BaseEntity> {
    private DatabaseTable databaseTable;
    private Runnable onSuccess;
    private OnErrorCallback<Exception> onError;
    private T entity;
}
