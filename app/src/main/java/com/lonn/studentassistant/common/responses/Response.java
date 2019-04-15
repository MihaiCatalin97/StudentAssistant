package com.lonn.studentassistant.common.responses;

import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;

public class Response<T extends BaseEntity>
{
    public Class type;
    public String action;
    public String result;
    public List<T> items;

    Response(String action, String result)
    {
        this.action = action;
        this.result = result;
    }

    public Response(Class type, String action, String result, List<T> items)
    {
        this.type = type;
        this.action = action;
        this.result = result;
        this.items = items;
    }
}
