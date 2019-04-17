package com.lonn.studentassistant.common.responses;

import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;

public class DeleteResponse<T extends BaseEntity> extends DatabaseResponse<T>
{
    public DeleteResponse(String result, List<T> items)
    {
        super("delete", result, items);
    }
    public DeleteResponse(String result, T item)
    {
        super("delete", result, item);
    }
}
