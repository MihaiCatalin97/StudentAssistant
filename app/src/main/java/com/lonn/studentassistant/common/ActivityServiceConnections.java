package com.lonn.studentassistant.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.lonn.studentassistant.common.abstractions.Response;
import com.lonn.studentassistant.services.abstractions.BasicService;
import com.lonn.studentassistant.activities.abstractions.ICallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityServiceConnections
{
    private List<CustomServiceConnection> serviceConnections = new ArrayList<>();
    private List<Class> serviceClasses = new ArrayList<>();

    public ActivityServiceConnections(Class... classes)
    {
        serviceClasses.addAll(Arrays.asList(classes));
    }

    public <T extends Response> void bind(ICallback<T> callback, ContextWrapper contextWrapper)
    {
        for (Class c : serviceClasses)
        {
            CustomServiceConnection<T> connection = new CustomServiceConnection<>(c, callback);

            Intent intent = new Intent(contextWrapper, c);

            contextWrapper.bindService(intent, connection, Context.BIND_AUTO_CREATE);

            serviceConnections.add(connection);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Response> void unbind(ICallback<T> callback, ContextWrapper wrapper)
    {
        for (CustomServiceConnection<T> connection : serviceConnections)
        {
            if (connection.service != null) {
                connection.service.removeCallback(callback);
                connection.service = null;

                wrapper.unbindService(connection);
            }
        }
    }

    private class CustomServiceConnection<T extends Response> implements ServiceConnection
    {
        BasicService<T> service;
        private Class serviceClass;
        private ICallback<T> callback;

        CustomServiceConnection(Class serviceClass, ICallback<T> callback)
        {
            this.serviceClass = serviceClass;
            this.callback = callback;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.e(serviceClass.getSimpleName() + " SERVICE CONNECT","!!!!!!!!!!!");

            service = ((LocalBinder) binder).getService();
            service.addCallback(callback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service.removeCallback(callback);
        }
    }

    public BasicService getServiceByClass(Class c)
    {
        for(CustomServiceConnection connection : serviceConnections)
        {
            if (connection.serviceClass.equals(c))
                return connection.service;
        }

        return null;
    }
}
