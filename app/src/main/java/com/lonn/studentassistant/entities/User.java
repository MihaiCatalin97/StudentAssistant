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

    private String personId;
    private String privileges;

    public User(String email, String personId, String privileges)
    {
        this.personId = personId;
        this.email = email;
        this.privileges = privileges;
    }

    public String getPersonId()
    {
        return personId;
    }

    public User()
    {}

    public String getPrivileges() {
        return privileges;
    }

    @Exclude
    public String getKey()
    {
        return Utils.emailToKey(email);
    }

    @Exclude
    public void setKey(String key)
    {
        email = Utils.keyToEmail(key);
    }
}
