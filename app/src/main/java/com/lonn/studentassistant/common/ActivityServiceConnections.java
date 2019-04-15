package com.lonn.studentassistant.common;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.lonn.studentassistant.common.abstractClasses.BasicService;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;

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

    public void bind(final IServiceCallback activity)
    {
        for (final Class c : serviceClasses)
        {
            CustomServiceConnection connection = new CustomServiceConnection(c, activity);

            Intent intent = new Intent((ContextWrapper)activity, c);

            ((ContextWrapper)activity).bindService(intent, connection, Context.BIND_AUTO_CREATE);

            serviceConnections.add(connection);
        }
    }

    public void unbind(IServiceCallback activity)
    {
        for (CustomServiceConnection connection : serviceConnections)
        {
            if (connection.service != null) {
                connection.service.removeCallback(activity);
                connection.service = null;

                ((ContextWrapper)activity).unbindService(connection);
            }
        }
    }

    private class CustomServiceConnection implements ServiceConnection
    {
        BasicService service;
        private Class serviceClass;
        private IServiceCallback callback;

        CustomServiceConnection(Class serviceClass, IServiceCallback callback)
        {
            this.serviceClass = serviceClass;
            this.callback = callback;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.e(serviceClass.getSimpleName() + " SERVICE CONNECT","!!!!!!!!!!!");

            service = ((LocalBinder)binder).getService();
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
