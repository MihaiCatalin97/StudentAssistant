package com.lonn.studentassistant.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

public class BaseEntity
{
    @Exclude
    private UUID id;

    protected BaseEntity()
    {
        id = UUID.randomUUID();
    }

    protected BaseEntity(UUID id)
    {
        this.id = id;
    }

    public UUID getId()
    {
        return id;
    }
}
