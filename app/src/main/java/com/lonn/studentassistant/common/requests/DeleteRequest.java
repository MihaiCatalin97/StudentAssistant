package com.lonn.studentassistant.common.requests;

import com.lonn.studentassistant.common.abstractions.Request;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;

public class DeleteRequest<T extends BaseEntity> extends Request
{
    private List<T> items;

    public DeleteRequest(List<T> items)
    {
        super("delete");
        this.items = items;
    }

    public List<T> getItems()
    {
        return items;
    }
}
