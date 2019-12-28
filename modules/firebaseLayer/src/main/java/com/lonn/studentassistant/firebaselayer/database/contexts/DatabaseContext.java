package com.lonn.studentassistant.firebaselayer.database.contexts;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseContext<T extends BaseEntity> implements IDatabaseContext<T> {
    @Getter(AccessLevel.PUBLIC)
    private DatabaseReference database = null;

    @Getter(AccessLevel.PROTECTED)
    private Class<T> modelClass;

    public DatabaseContext(String databaseTableReference, Class<T> modelClass) {
        try {
            database = FirebaseDatabase.getInstance().getReference(databaseTableReference);
            this.modelClass = modelClass;
        } catch (IllegalStateException | NullPointerException e) {
            log.error("Error while instantiating IDatabaseContext for table {}:\n{}",
                    databaseTableReference,
                    e);
        } finally {
            log.info("Context connected to table {}", databaseTableReference);
        }
    }

    @Override
    public void saveOrUpdate(T entity,
                             Runnable onSuccess,
                             Consumer<Exception> onError) {
        if (getDatabase() != null) {
            Task<Void> dbTask = getDatabase().child(entity
                    .getKey())
                    .setValue(entity);

            addListenersToTask(dbTask, onSuccess, onError);
        }
    }

    @Override
    public void delete(String key,
                       Runnable onSuccess,
                       Consumer<Exception> onError) {
        if (getDatabase() != null) {
            Task<Void> dbTask = getDatabase().child(key).removeValue();

            addListenersToTask(dbTask, onSuccess, onError);
        }
    }

    @Override
    public void get(Consumer<List<T>> onSuccess,
                    Consumer<DatabaseError> onError,
                    Boolean subscribe) {
        get(onSuccess, onError, null, subscribe);
    }

    @Override
    public void get(Consumer<List<T>> onSuccess,
                    Consumer<DatabaseError> onError,
                    Predicate<? super T> predicate,
                    Boolean subscribe) {
        Query queryReference = getDatabase();
        if (queryReference != null) {

            if (predicate != null) {
                queryReference = predicate.apply(getDatabase());
            }

            queryReference.addValueEventListener(new CustomValueEventListener(onSuccess, onError, subscribe));
        }
    }

    @Override
    public void deleteAll(Runnable onSuccess,
                          Consumer<Exception> onError) {
        if (getDatabase() != null) {
            Task<Void> dbTask = getDatabase().removeValue();

            addListenersToTask(dbTask, onSuccess, onError);
        }
    }

    void addListenersToTask(Task<Void> dbTask,
                            Runnable onSuccess,
                            Consumer<Exception> onError) {
        if (onSuccess != null) {
            dbTask.addOnCompleteListener((Task<Void> task) -> onSuccess.run());
        }
        if (onError != null) {
            dbTask.addOnFailureListener(onError::consume);
        }
    }

    @AllArgsConstructor
    class CustomValueEventListener implements ValueEventListener {
        private Consumer<List<T>> onSuccess;
        private Consumer<DatabaseError> onError;
        private Boolean subscribe;

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (!subscribe) {
                dataSnapshot.getRef().removeEventListener(this);
            }

            if (onSuccess != null) {
                List<T> entities = new ArrayList<>();

                for (DataSnapshot snapEntity : dataSnapshot.getChildren()) {
                    T entity = snapEntity.getValue(getModelClass());

                    if (entity != null) {
                        entity.setKey(snapEntity.getKey());
                        entities.add(entity);
                    }
                }

                onSuccess.consume(entities);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            if (onError != null) {
                onError.consume(databaseError);
            }
        }
    }
}
