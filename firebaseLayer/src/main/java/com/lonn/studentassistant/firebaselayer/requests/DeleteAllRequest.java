package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.interfaces.OnErrorCallback;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DeleteAllRequest {
    private DatabaseTable databaseTable;
    private OnErrorCallback<Exception> onError;
    private Runnable onSuccess;
}
