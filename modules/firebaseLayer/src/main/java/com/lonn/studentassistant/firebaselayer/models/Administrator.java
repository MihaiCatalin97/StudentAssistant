package com.lonn.studentassistant.firebaselayer.models;

import com.lonn.studentassistant.firebaselayer.models.abstractions.HashableEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Administrator extends HashableEntity {
    private String firstName;
    private String lastName;
    private String administratorKey;

    @Override
    public String computeHashingString() {
        return getFirstName() + getLastName() + getAdministratorKey();
    }
}
