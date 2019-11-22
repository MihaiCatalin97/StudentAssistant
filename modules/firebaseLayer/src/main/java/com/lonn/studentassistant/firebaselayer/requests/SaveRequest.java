package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SaveRequest<T extends BaseEntity> {
    private DatabaseTable databaseTable;
    private Runnable onSuccess;
    private Consumer<Exception> onError;
    private T entity;

    public SaveRequest<T> entity(T entity) {
        this.entity = entity;
        this.databaseTable = DatabaseTableContainer.valueOf(entity.getClass());

        return this;
    }
}
