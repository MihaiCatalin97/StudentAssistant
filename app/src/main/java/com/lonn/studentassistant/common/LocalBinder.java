package com.lonn.studentassistant.common;

import android.os.Binder;

import com.lonn.studentassistant.common.abstractions.Response;
import com.lonn.studentassistant.services.abstractions.BasicService;
import com.lonn.studentassistant.entities.BaseEntity;

public class LocalBinder<T extends Response> extends Binder
{
    private BasicService<T> service;

    public LocalBinder(BasicService<T> service)
    {
        this.service = service;
    }
    public BasicService<T> getService() {
        return service;
    }
}