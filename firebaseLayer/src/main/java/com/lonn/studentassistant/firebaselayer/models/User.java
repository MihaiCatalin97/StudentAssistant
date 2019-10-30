package com.lonn.studentassistant.firebaselayer.models;

import com.google.firebase.database.Exclude;
import com.lonn.studentassistant.firebaselayer.Utils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends BaseEntity {
    private String email;
    private String privileges;

    private String personId;

    @Exclude
    public String computeKey() {
        return Utils.emailToKey(email);
    }
}
