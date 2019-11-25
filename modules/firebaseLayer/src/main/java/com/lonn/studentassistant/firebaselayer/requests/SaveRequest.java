package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SaveRequest<T extends BaseEntity> {
    private DatabaseTable databaseTable;
    private Runnable onSuccess;
    private Consumer<Exception> onError;
    private List<T> entities = new ArrayList<>();

    public SaveRequest<T> entities(List<T> entities) {
        if (entities != null) {
            this.entities.addAll(entities);
            setDatabaseTable(entities.get(0)
                    .getClass());
        }
        return this;
    }

    public SaveRequest<T> entity(T entity) {
        if (entity != null) {
            entities.add(entity);
            setDatabaseTable(entity.getClass());
        }
        return this;
    }

    private void setDatabaseTable(Class<? extends BaseEntity> entityClass) {
        if (databaseTable == null) {
            this.databaseTable = DatabaseTableContainer.valueOf(entityClass);
        }
    }
}
