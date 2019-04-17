package com.lonn.studentassistant.common.responses;

import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;

public class GetByIdResponse<T extends BaseEntity> extends DatabaseResponse<T>
{
    public GetByIdResponse(String result, List<T> items)
    {
        super("getById", result, items);
    }
    public GetByIdResponse(String result, T item)
    {
        super("getById", result, item);
    }
}
