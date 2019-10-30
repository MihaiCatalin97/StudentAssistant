package com.lonn.studentassistant.firebaselayer.firebaseConnection.contexts;


import com.google.firebase.database.DatabaseError;
import com.lonn.studentassistant.firebaselayer.interfaces.OnErrorCallback;
import com.lonn.studentassistant.firebaselayer.interfaces.OnSuccessCallback;
import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;

import java.util.List;
import java.util.UUID;

public interface IDatabaseContext<T extends BaseEntity> {
    void saveOrUpdate(T entity, Runnable onSuccess, OnErrorCallback<Exception> onError);

    void delete(String key, Runnable onSuccess, OnErrorCallback<Exception> onError);

    void get(OnSuccessCallback<List<T>> onSuccess, OnErrorCallback<DatabaseError> onError);

    void get(OnSuccessCallback<List<T>> onSuccess, OnErrorCallback<DatabaseError> onError, Predicate<T> predicate);

    void deleteAll(Runnable onSuccess, OnErrorCallback<Exception> onError);
}
