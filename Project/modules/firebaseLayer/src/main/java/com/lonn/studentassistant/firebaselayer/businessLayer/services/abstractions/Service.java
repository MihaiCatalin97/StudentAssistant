package com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions;

import android.os.Handler;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.Future;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.tasks.FirebaseTask;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.tasks.FirebaseVoidTask;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.AuthenticationService;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.DeleteAllRequest;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.DeleteByIdRequest;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.requests.SaveRequest;

import java.util.LinkedList;
import java.util.List;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel.READ_PUBLIC;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel.WRITE;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel.WRITE_ENROLL;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.fields.BaseEntityField.ID;

public abstract class Service<T extends BaseEntity, V extends Exception, U extends EntityViewModel<T>>
        implements IService<T, V, U> {
    protected FirebaseConnection firebaseConnection;
    protected AuthenticationService authenticationService;
    protected ViewModelAdapter<T, U> adapter;

    protected Service(FirebaseConnection firebaseConnection) {
        this.firebaseConnection = firebaseConnection;
    }

    @Override
    public Future<List<U>, Exception> getAll(boolean subscribe) {
        Future<List<U>, Exception> result = new Future<>();

        firebaseConnection.execute(new GetRequest<T, V>()
                .databaseTable(getDatabaseTable())
                .subscribe(subscribe)
                .onSuccess(entities -> {
                    if (entities != null) {
                        List<T> visibleEntities = new LinkedList<>();

                        for (T entity : entities) {
                            if (getPermissionLevel(entity).isAtLeast(READ_PUBLIC)) {
                                visibleEntities.add(entity);
                            }
                        }

                        result.complete(transform(visibleEntities));
                    }
                    else {
                        result.completeExceptionally(new Exception("Entity not found"));
                    }
                }));

        return result;
    }

    @Override
    public Future<U, Exception> getById(String id, boolean subscribe) {
        Future<U, Exception> result = new Future<>();

        firebaseConnection.execute(new GetRequest<T, V>()
                .databaseTable(getDatabaseTable())
                .predicate(where(ID).equalTo(id))
                .subscribe(subscribe)
                .onSuccess(entities -> {
                    if (entities != null && entities.size() == 1) {
                        if (getPermissionLevel(entities.get(0)).isAtLeast(READ_PUBLIC)) {
                            result.complete(transform(entities.get(0)));
                        }
                        else {
                            result.completeExceptionally(new Exception("Insufficient privileges"));
                        }
                    }
                    else {
                        result.completeExceptionally(new Exception("Entity not found"));
                    }
                }));

        return result;
    }

    @Override
    public Future<Void, Exception> save(U entityViewModel) {
        Future<Void, Exception> result = new Future<>();

        if (getPermissionLevel(adapter.adapt(entityViewModel)).isAtLeast(WRITE_ENROLL)) {
            firebaseConnection.execute(new SaveRequest<>()
                    .databaseTable(getDatabaseTable())
                    .entity(adapter.adapt(entityViewModel))
                    .onSuccess(result::complete)
                    .onError(result::completeExceptionally));
        }
        else {
            new Handler().postDelayed(() ->
                            result.completeExceptionally(new Exception("Insufficient privileges")),
                    500);
        }

        return result;
    }

    @Override
    public FirebaseTask<Void, V> save(T entity) {
        if (getPermissionLevel(entity).isAtLeast(WRITE_ENROLL)) {
            return new FirebaseVoidTask<>(firebaseConnection,
                    new SaveRequest<T, V>()
                            .databaseTable(getDatabaseTable())
                            .entity(entity));
        }
        else {
            return null;
        }
    }

    @Override
    public FirebaseTask<Void, Exception> deleteAll() {
        return new FirebaseVoidTask<>(firebaseConnection,
                new DeleteAllRequest()
                        .databaseTable(getDatabaseTable()));
    }

    @Override
    public Future<Void, Exception> deleteById(String id) {
        Future<Void, Exception> result = new Future<>();

        getById(id, false)
                .onSuccess(entity -> {
                    if (getPermissionLevel(adapter.adapt(entity)).isAtLeast(WRITE)) {
                        firebaseConnection.execute(new DeleteByIdRequest()
                                .databaseTable(getDatabaseTable())
                                .key(id)
                                .onSuccess(result::complete)
                                .onError(result::completeExceptionally));
                    }
                    else {
                        result.completeExceptionally(new Exception("Insufficient privileges"));
                    }
                })
                .onError(result::completeExceptionally);

        return result;
    }

    @Override
    public Future<List<U>, Exception> getByIds(List<String> gradeKeys, boolean subscribe) {
        Future<List<U>, Exception> result = new Future<>();
        final List<U> grades = new LinkedList<>();
        final List<Exception> errors = new LinkedList<>();

        for (String gradeKey : gradeKeys) {
            getById(gradeKey, subscribe)
                    .onSuccess(grade -> {
                        grades.add(grade);

                        if (grades.size() + errors.size() == gradeKeys.size()) {
                            result.complete(grades);
                        }
                    })
                    .onError(error -> {
                        errors.add(error);

                        if (errors.size() == gradeKeys.size()) {
                            result.completeExceptionally(errors.get(0));
                        }
                        if (grades.size() + errors.size() == gradeKeys.size()) {
                            result.complete(grades);
                        }
                    });
        }

        return result;
    }

    protected void init() {
        authenticationService = AuthenticationService.getInstance(firebaseConnection);
    }

    protected U transform(T entity) {
        return adapter.adapt(entity);
    }

    protected List<U> transform(List<T> entities) {
        return adapter.adapt(entities);
    }

    protected abstract DatabaseTable<T> getDatabaseTable();

    protected abstract PermissionLevel getPermissionLevel(T entity);
}
