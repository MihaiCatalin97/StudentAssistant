package com.lonn.studentassistant.common.responses;

import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.requests.CreateRequest;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;

public class CreateResponse<T extends BaseEntity> extends DatabaseResponse<T>
{
    public CreateResponse(String result, List<T> items)
    {
        super("create", result, items);
    }
    public CreateResponse(String result, T item)
    {
        super("create", result, item);
    }
}
