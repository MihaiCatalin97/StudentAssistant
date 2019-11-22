package com.lonn.studentassistant.firebaselayer.requests;

import com.google.firebase.database.DatabaseError;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class GetRequest<T extends BaseEntity> {
    private DatabaseTable databaseTable;
    private Boolean subscribe = true;
    private Consumer<List<T>> onSuccess;
    private Consumer<DatabaseError> onError;
    private Predicate<T> predicate;
}
