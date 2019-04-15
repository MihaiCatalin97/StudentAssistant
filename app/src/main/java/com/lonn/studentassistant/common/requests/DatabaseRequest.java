package com.lonn.studentassistant.common.requests;

import com.lonn.studentassistant.entities.BaseEntity;

public class DatabaseRequest<T extends BaseEntity> extends Request
{
    public T item;
    public String key;

    public DatabaseRequest(String action, T item)
    {
        super(action);
        this.item = item;
    }

    public DatabaseRequest(String action, String key)
    {
        super(action);
        this.key = key;
    }
}
