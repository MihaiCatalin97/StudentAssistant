package com.lonn.studentassistant.firebaselayer.database.contexts;


import com.google.firebase.database.DatabaseError;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;

import java.util.List;

public interface IDatabaseContext<T extends BaseEntity> {
    void saveOrUpdate(T entity, Runnable onSuccess, Consumer<Exception> onError);

    void delete(String key, Runnable onSuccess, Consumer<Exception> onError);

    void get(Consumer<List<T>> onSuccess, Consumer<DatabaseError> onError, Boolean subscribe);

    void get(Consumer<List<T>> onSuccess, Consumer<DatabaseError> onError, Predicate<T> predicate, Boolean subscribe);

    void deleteAll(Runnable onSuccess, Consumer<Exception> onError);
}
