package com.lonn.studentassistant.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.lonn.studentassistant.common.abstractions.Request;
import com.lonn.studentassistant.common.abstractions.Response;
import com.lonn.studentassistant.common.requests.CreateRequest;
import com.lonn.studentassistant.common.requests.CredentialsRequest;
import com.lonn.studentassistant.common.requests.DeleteRequest;
import com.lonn.studentassistant.common.requests.EditRequest;
import com.lonn.studentassistant.common.requests.GetAllRequest;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.common.requests.LoginRequest;
import com.lonn.studentassistant.services.abstractions.BasicService;
import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.services.abstractions.DatabaseService;
import com.lonn.studentassistant.services.implementations.credentialsCheckService.CredentialsCheckService;
import com.lonn.studentassistant.services.implementations.loginService.LoginService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConnectionBundle
{
    private static Map<BasicService, CustomServiceConnection> services = new HashMap<>();
    private Map<Class, List<Request>> requestMap = new HashMap<>();
    private Context context;

    public ConnectionBundle(Context context)
    {
        this.context = context;
    }

    public <T extends Response> void bind(Class<?> serviceClass, ICallback<T> callback)
    {
        if(getServiceByClass(serviceClass) == null)
        {
            CustomServiceConnection<T> connection = new CustomServiceConnection<>(callback);

            Intent intent = new Intent(context, serviceClass);

            Log.e("binding", serviceClass.getSimpleName());

            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

            try
            {
                services.put((BasicService)serviceClass.getConstructor().newInstance(), connection);

                printServices();
            }
            catch (Exception e)
            {
                Toast.makeText(context, "An error occured!\n Error code 200", Toast.LENGTH_LONG).show();
                Log.e("Error services","");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Response> void unbind(ICallback<T> callback, ContextWrapper wrapper)
    {
        List<BasicService> servicesToUnbind = new LinkedList<>();

        Log.e("unbind Called", callback.getClass().getSimpleName());
        for(BasicService service : services.keySet())
        {
            CustomServiceConnection connection = services.get(service);

            if (connection != null && connection.callback.equals(callback))
            {
                servicesToUnbind.add(service);
            }
        }

        printServices();

        for (BasicService service : servicesToUnbind)
        {
            Log.e("unbinding", service.getClass().getSimpleName());

            wrapper.unbindService(services.get(service));
            services.remove(service);
        }
    }

    private class CustomServiceConnection<T extends Response> implements ServiceConnection
    {
        private ICallback<T> callback;

        CustomServiceConnection(ICallback<T> callback)
        {
            this.callback = callback;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onServiceConnected(ComponentName name, IBinder binder) {
            BasicService<T> service = ((LocalBinder) binder).getService();

            services.remove(getServiceByClass(service.getClass()));
            printServices();

            service.addCallback(callback);
            services.put(service, this);

            if(service instanceof DatabaseService)
                ((DatabaseService) service).onConnected();

            if (requestMap.containsKey(service.getClass()))
            {
                List<Request> requestList = requestMap.get(service.getClass());

                if (requestList != null)
                {
                    for (Request req : requestList)
                    {
                        postRequest(service.getClass(), req);
                    }
                }
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onServiceDisconnected(ComponentName name)
        {
            for(BasicService service : services.keySet())
            {
                CustomServiceConnection<T> mapCallback = services.get(service);

                if (mapCallback != null && mapCallback.equals(this))
                {
                    service.removeCallback(callback);
                    services.remove(service);
                }
            }
        }
    }

    public BasicService getServiceByClass(Class c)
    {
        for(BasicService service : services.keySet())
        {
            if (service.getClass().equals(c))
                return service;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public void postRequest(Class serviceClass, Request request)
    {
        BasicService service = getServiceByClass(serviceClass);

        if(service == null)
        {
            List<Request> newList = requestMap.get(serviceClass);

            if (requestMap.get(serviceClass) == null)
            {
                requestMap.put(serviceClass, newList);
            }

            requestMap.get(serviceClass).add(request);
        }
        else if (service instanceof DatabaseService)
        {
            try
            {
                if (request instanceof CreateRequest)
                    ((DatabaseService) service).postRequest((CreateRequest) request);
                if (request instanceof DeleteRequest)
                    ((DatabaseService) service).postRequest((DeleteRequest) request);
                if (request instanceof EditRequest)
                    ((DatabaseService) service).postRequest((EditRequest) request);
                if (request instanceof GetAllRequest)
                    ((DatabaseService) service).postRequest((GetAllRequest) request);
                if (request instanceof GetByIdRequest)
                    ((DatabaseService) service).postRequest((GetByIdRequest) request);
            }
            catch (Exception e)
            {
                Toast.makeText(context, "An error occured!\n Error code 100", Toast.LENGTH_LONG).show();
                Log.e("Error code 1", e.getLocalizedMessage());
            }
        }
        else if (service instanceof LoginService && request instanceof LoginRequest)
            ((LoginService) service).postRequest((LoginRequest)request);
        else if (service instanceof CredentialsCheckService && request instanceof CredentialsRequest)
            ((CredentialsCheckService) service).postRequest((CredentialsRequest)request);
    }

    private void printServices()
    {
        Log.e("Current services", Integer.toString(services.size()));

        for(BasicService service : services.keySet())
        {
            Log.e("Service", service.getClass().getSimpleName());
        }
    }
}
