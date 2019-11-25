package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.entities.IdentificationHash;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class CredentialsCheckRequest {
    private String identificationHash;
    private Consumer<IdentificationHash> onSuccess;
    private Consumer<String> onError;
}
