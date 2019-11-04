package com.lonn.studentassistant.firebaselayer.requests;

import com.google.firebase.auth.FirebaseUser;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.models.IdentificationHash;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class RegisterRequest {
    private String email;
    private String password;
    private String personUUID;
    private Consumer<FirebaseUser> onSuccess;
    private Consumer<String> onError;
}
