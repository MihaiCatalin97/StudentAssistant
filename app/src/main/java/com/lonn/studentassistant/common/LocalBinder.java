package com.lonn.studentassistant.common;

import android.os.Binder;

import com.lonn.studentassistant.common.abstractClasses.BasicService;
import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.entities.BaseEntity;

public class LocalBinder<T extends BaseEntity> extends Binder
{
    private BasicService service;

    public LocalBinder(BasicService service)
    {
        this.service = service;
    }
    public BasicService getService() {
        return service;
    }
}