package com.lonn.studentassistant.common.requests;

import com.lonn.studentassistant.common.abstractions.Request;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class CreateRequest<T extends BaseEntity> extends Request
{
    private List<T> items;

    public CreateRequest(List<T> items)
    {
        super("create");
        this.items = new ArrayList<>(items);
    }

    public List<T> getItems()
    {
        return items;
    }
}
