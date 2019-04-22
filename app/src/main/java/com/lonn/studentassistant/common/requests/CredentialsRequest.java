package com.lonn.studentassistant.common.requests;

import com.lonn.studentassistant.common.abstractions.Request;
import com.lonn.studentassistant.entities.BaseEntity;

public class CredentialsRequest<T extends BaseEntity> extends Request
{
    private T entity;

    public CredentialsRequest(T entity)
    {
        super("credentials");
        this.entity = entity;
    }

    public T getEntity()
    {
        return entity;
    }
}
