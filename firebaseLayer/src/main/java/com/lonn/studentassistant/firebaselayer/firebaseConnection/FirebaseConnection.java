package com.lonn.studentassistant.firebaselayer.firebaseConnection;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.firebaselayer.config.FirebaseConfig;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.contexts.DatabaseContext;
import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.firebaselayer.requests.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.requests.DeleteAllRequest;
import com.lonn.studentassistant.firebaselayer.requests.DeleteByIdRequest;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.LoginRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirebaseConnection {
    @Getter(AccessLevel.PROTECTED)
    private Map<DatabaseTable, DatabaseContext> databaseMap = new HashMap<>();

    public FirebaseConnection(Context applicationContext) {
        FirebaseConfig firebaseConfig = new FirebaseConfig(applicationContext);

        for (DatabaseTable table : DatabaseTable.values()) {
            databaseMap.put(table,
                    new DatabaseContext<>(firebaseConfig.getTableReference(table),
                            table.getTableClass()));
        }
    }

    public <T extends BaseEntity> void execute(GetRequest<T> request) {
        @SuppressWarnings("unchecked")
        DatabaseContext<T> context = getDatabaseMap().get(request.databaseTable());

        if (context != null) {
            context.get(request.onSuccess(), request.onError(), request.predicate());
        }
    }

    public <T extends BaseEntity> void execute(SaveRequest<T> request) {
        @SuppressWarnings("unchecked")
        DatabaseContext<T> context = getDatabaseMap().get(request.databaseTable());

        if (context != null) {
            context.saveOrUpdate(request.entity(), request.onSuccess(), request.onError());
        }
    }

    public void execute(DeleteByIdRequest request) {
        DatabaseContext context = getDatabaseMap().get(request.databaseTable());

        if (context != null) {
            context.delete(request.key(), request.onSuccess(), request.onError());
        }
    }

    public void execute(DeleteAllRequest request) {
        DatabaseContext context = getDatabaseMap().get(request.databaseTable());

        if (context != null) {
            context.deleteAll(request.onSuccess(), request.onError());
        }
    }

    public void execute(final LoginRequest request) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(request.username(), request.password())
                .addOnCompleteListener((loginTask) -> {
                    if (loginTask.isSuccessful()) {
                        Log.i("Login", "Login successful for user " + request.username());
                        request.onSuccess().run();
                    }
                    else {
                        Log.i("Login", "Login error for user " + request.username());
                        request.onError().run();
                    }
                });
    }
}