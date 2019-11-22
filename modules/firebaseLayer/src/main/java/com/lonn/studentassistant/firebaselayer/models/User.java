package com.lonn.studentassistant.firebaselayer.models;

import com.google.firebase.database.Exclude;
import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends BaseEntity {
    @Exclude
    private String userId;
    private String personUUID;

    @Exclude
    public String getUserId(){
        return userId;
    }

    @Exclude
    public String getKey() {
        return userId;
    }
}
