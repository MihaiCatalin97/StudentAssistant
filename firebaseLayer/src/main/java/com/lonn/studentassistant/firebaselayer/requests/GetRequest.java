package com.lonn.studentassistant.firebaselayer.requests;

import com.google.firebase.database.DatabaseError;
import com.lonn.studentassistant.firebaselayer.interfaces.OnErrorCallback;
import com.lonn.studentassistant.firebaselayer.interfaces.OnSuccessCallback;
import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class GetRequest<T extends BaseEntity> {
    private DatabaseTable databaseTable;
    private OnSuccessCallback<List<T>> onSuccess;
    private OnErrorCallback<DatabaseError> onError;
    private Predicate<T> predicate;
}
