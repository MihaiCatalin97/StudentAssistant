package com.lonn.studentassistant.common.abstractions;

import com.lonn.studentassistant.entities.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseResponse<T extends BaseEntity> extends Response
{
    private List<T> items;

    protected DatabaseResponse(String action, String result, List<T> items)
    {
        super(action, result);
        this.items = items;
    }

    protected DatabaseResponse(String action, String result, T item)
    {
        super(action, result);
        items = new ArrayList<>();
        items.add(item);
    }

    public List<T> getItems()
    {
        return items;
    }
}
