package com.lonn.studentassistant.common.abstractClasses;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lonn.studentassistant.common.LocalBinder;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class LocalService<T extends BaseEntity> extends Service
{
    private final IBinder binder = new LocalBinder<>(this);
    protected List<IServiceCallback> serviceCallbacks;
    protected AbstractRepository<T> repository;

    @Override
    public void onCreate()
    {
        super.onCreate();
        serviceCallbacks = new ArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void addCallback(IServiceCallback callback) {
        serviceCallbacks.add(callback);

        if (repository == null)
        {
            repository = instantiateRepository();
        }
    }

    public void removeCallback(IServiceCallback callback)
    {
        serviceCallbacks.remove(callback);
    }

    protected abstract AbstractRepository<T> instantiateRepository();
}
