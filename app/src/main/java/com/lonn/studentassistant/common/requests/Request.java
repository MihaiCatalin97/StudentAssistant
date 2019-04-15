package com.lonn.studentassistant.common.requests;

import com.lonn.studentassistant.entities.BaseEntity;

public class Request<T extends BaseEntity>
{
    public String action;

    public Request(String action)
    {
        this.action = action;
    }
}
