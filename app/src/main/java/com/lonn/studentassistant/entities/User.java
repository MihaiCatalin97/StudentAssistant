package com.lonn.studentassistant.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

@IgnoreExtraProperties
public class User
{
    @Exclude
    public UUID id;

    @Exclude
    public String email;

    public String privileges;

    public User(String email, String privileges)
    {
        this.id = UUID.randomUUID();
        this.email = email;
        this.privileges = privileges;
    }

    public User()
    {}
}
