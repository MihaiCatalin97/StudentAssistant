package com.lonn.studentassistant.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.lonn.studentassistant.common.Utils;

import java.io.Serializable;
import java.util.UUID;

@IgnoreExtraProperties
public class User extends BaseEntity
{
    @Exclude
    public String email;

    private String studentId;
    private String privileges;

    public User(String email, String studentId, String privileges)
    {
        this.studentId = studentId;
        this.email = email;
        this.privileges = privileges;
    }

    public User()
    {}

    public String getPrivileges() {
        return privileges;
    }

    public String getKey()
    {
        return Utils.emailToKey(email);
    }

    public void setKey(String key)
    {
        email = Utils.keyToEmail(key);
    }
}
