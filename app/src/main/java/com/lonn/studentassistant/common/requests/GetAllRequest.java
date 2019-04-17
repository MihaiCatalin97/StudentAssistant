package com.lonn.studentassistant.common.requests;

import com.lonn.studentassistant.common.abstractions.Request;
import com.lonn.studentassistant.entities.BaseEntity;

public class GetAllRequest<T extends BaseEntity> extends Request
{
    public GetAllRequest()
    {
        super("getAll");
    }
}
