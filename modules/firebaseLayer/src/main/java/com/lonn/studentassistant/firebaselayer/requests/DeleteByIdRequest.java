package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DeleteByIdRequest {
    private DatabaseTable databaseTable;
    private String key;
    private Consumer<Exception> onError;
    private Runnable onSuccess;
}
