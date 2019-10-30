package com.lonn.studentassistant.firebaselayer.requests;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class LoginRequest {
    private String username;
    private String password;
    private Runnable onSuccess;
    private Runnable onError;
}
