package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.interfaces.OnErrorCallback;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DeleteRequest {
    private DatabaseTable databaseTable;
    private String key;
    private OnErrorCallback<Exception> onError;
    private Runnable onSuccess;
}
