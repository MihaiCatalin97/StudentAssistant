package com.lonn.studentassistant.common.requests;

import com.lonn.studentassistant.common.abstractions.Request;
import com.lonn.studentassistant.entities.BaseEntity;

public class GetByIdRequest<T extends BaseEntity> extends Request
{
    private String key;

    public GetByIdRequest(String key)
    {
        super("getById");
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }
}
