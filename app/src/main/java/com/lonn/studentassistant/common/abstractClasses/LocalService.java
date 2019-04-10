package com.lonn.studentassistant.common.abstractClasses;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lonn.studentassistant.common.LocalBinder;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;
import com.lonn.studentassistant.entities.BaseEntity;

public abstract class LocalService<T extends BaseEntity> extends Service
{
    private final IBinder binder = new LocalBinder<>(this);
    protected IServiceCallback<T> serviceCallbacks;
    protected AbstractRepository<T> repository;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setCallbacks(IServiceCallback<T> callbacks) {
        serviceCallbacks = callbacks;

        if (repository == null)
        {
            repository = instantiateRepository();
        }
    }

    protected abstract AbstractRepository<T> instantiateRepository();
}
