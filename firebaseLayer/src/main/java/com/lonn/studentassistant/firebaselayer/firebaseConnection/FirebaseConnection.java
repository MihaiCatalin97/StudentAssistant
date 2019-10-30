package com.lonn.studentassistant.firebaselayer.firebaseConnection;

import com.lonn.studentassistant.firebaselayer.config.FirebaseConfig;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.contexts.DatabaseContext;
import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.firebaselayer.requests.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.requests.DeleteRequest;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
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

    public FirebaseConnection(FirebaseConfig firebaseConfig) {
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

    public void execute(DeleteRequest request) {
        DatabaseContext context = getDatabaseMap().get(request.databaseTable());

        if (context != null) {
            context.delete(request.key(), request.onSuccess(), request.onError());
        }
    }
}