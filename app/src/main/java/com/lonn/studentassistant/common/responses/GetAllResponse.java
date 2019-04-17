package com.lonn.studentassistant.common.responses;

import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;

public class GetAllResponse<T extends BaseEntity> extends DatabaseResponse<T>
{
    public GetAllResponse(String result, List<T> items)
    {
        super("getAll", result, items);
    }
    public GetAllResponse(String result, T item)
    {
        super("getAll", result, item);
    }
}
