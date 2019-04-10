package com.lonn.studentassistant.common;

import android.os.Binder;

import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.entities.BaseEntity;

public class LocalBinder<T extends BaseEntity> extends Binder
{
    private LocalService<T> service;

    public LocalBinder(LocalService<T> service)
    {
        this.service = service;
    }
    public LocalService<T> getService() {
        return service;
    }
}