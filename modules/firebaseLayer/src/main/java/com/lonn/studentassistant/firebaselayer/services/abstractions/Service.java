package com.lonn.studentassistant.firebaselayer.services.abstractions;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseListTask;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseTask;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseVoidTask;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.DeleteAllRequest;
import com.lonn.studentassistant.firebaselayer.requests.DeleteByIdRequest;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.List;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.STUDENTS;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public abstract class Service<T extends BaseEntity, V extends Exception, U extends EntityViewModel<T>> {
    protected FirebaseConnection firebaseConnection;
    protected ViewModelAdapter<T, U> adapter;

    protected Service(FirebaseConnection firebaseConnection) {
        this.firebaseConnection = firebaseConnection;
    }

    protected abstract void init();

    public FirebaseTask<List<U>, V> getAll() {
        return new FirebaseListTask<T, U, V>(firebaseConnection,
                new GetRequest<T, V>().databaseTable(getDatabaseTable()))
                .setTransformer(this::transform);
    }

    public Future<U, Exception> getById(String id, boolean subscribe) {
        Future<U, Exception> result = new Future<>();

        firebaseConnection.execute(new GetRequest<T, V>()
                .databaseTable(getDatabaseTable())
                .predicate(where(ID).equalTo(id))
                .subscribe(subscribe)
                .onSuccess(entities -> {
                    if (entities == null || entities.size() != 1) {
                        result.completeExceptionally(new Exception("Entity not found"));
                        return;
                    }

                    result.complete(transform(entities.get(0)));
                }));

        return result;
    }

    public Future<Void, Exception> save(U entityViewModel) {
        Future<Void, Exception> result = new Future<>();

        firebaseConnection.execute(new SaveRequest<>()
                .databaseTable(getDatabaseTable())
                .entity(adapter.adapt(entityViewModel))
                .onSuccess(result::complete)
                .onError(result::completeExceptionally));

        return result;
    }

    public FirebaseTask<Void, V> save(T entity) {
        return new FirebaseVoidTask<>(firebaseConnection,
                new SaveRequest<T, V>()
                        .databaseTable(getDatabaseTable())
                        .entity(entity));
    }

    public FirebaseTask<Void, Exception> deleteAll() {
        return new FirebaseVoidTask<>(firebaseConnection,
                new DeleteAllRequest()
                        .databaseTable(getDatabaseTable()));
    }

    public Future<Void, Exception> deleteById(String id) {
        Future<Void, Exception> result = new Future<>();

        firebaseConnection.execute(new DeleteByIdRequest()
                .databaseTable(getDatabaseTable())
                .key(id)
                .onSuccess(result::complete)
                .onError(result::completeExceptionally));

        return result;
    }

    protected U transform(T entity) {
        return adapter.adapt(entity);
    }

    protected List<U> transform(List<T> entities) {
        return adapter.adapt(entities);
    }

    protected Future<Boolean, Exception> exists(String entityKey) {
        Future<Boolean, Exception> result = new Future<>();

        firebaseConnection.execute(new GetRequest<Student, Exception>()
                .databaseTable(STUDENTS)
                .subscribe(false)
                .onSuccess(students -> {
                    for (Student student : students) {
                        if (student.getKey().equals(entityKey)) {
                            result.complete(true);
                            return;
                        }
                    }
                    result.complete(false);
                })
                .onError(result::completeExceptionally));

        return result;
    }

    protected abstract DatabaseTable<T> getDatabaseTable();
}
