package com.lonn.studentassistant.common.responses;

import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;

public class EditResponse<T extends BaseEntity> extends DatabaseResponse<T>
{
    public EditResponse(String result, List<T> items)
    {
        super("edit", result, items);
    }
    public EditResponse(String result, T item)
    {
        super("edit", result, item);
    }
}
