package com.lonn.studentassistant.common.responses;

import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;

public class CredentialsResponse<T extends BaseEntity> extends DatabaseResponse<T>
{
    public CredentialsResponse(String result, List<T> items)
    {
        super("credentials", result, items);
    }

    public CredentialsResponse(String result, T item)
    {
        super("credentials", result, item);
    }
}
