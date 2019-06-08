package com.lonn.studentassistant.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
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
    private static List<Class> waitingServices = new LinkedList<>();
    private static Map<BasicService, List<CustomServiceConnection>> services = new HashMap<>();
    private Map<Class, Map<Request, ICallback>> waitingRequests = new HashMap<>();
    private Context context;

    public ConnectionBundle(Context context)
    {
        this.context = context;
    }

    private <T extends  Response> void bindServiceWithIntent(Class<?> serviceClass, ICallback<T> callback)
    {
        CustomServiceConnection<T> connection = new CustomServiceConnection<>(callback);
        Intent intent = new Intent(context, serviceClass);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private <T extends Response> void bind(final Class<?> serviceClass, ICallback<T> callback)
    {
        final BasicService service = getServiceByClass(serviceClass);
        bindServiceWithIntent(serviceClass, callback);

        if(service == null)
            waitingServices.add(serviceClass);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(services.get(getServiceByClass(serviceClass)) != null && services.get(getServiceByClass(serviceClass)).size() > 0)
                    Log.e(serviceClass.getSimpleName() + " number of connections", Integer.toString(services.get(getServiceByClass(serviceClass)).size()));
                if(getServiceByClass(serviceClass) != null && getServiceByClass(serviceClass).getCallbackCount() > 0)
                    Log.e(serviceClass.getSimpleName() + " number of callbacks", Integer.toString(getServiceByClass(serviceClass).getCallbackCount()));
                new Handler().postDelayed(this,5000);
            }
        },5000);
    }

    @SuppressWarnings("unchecked")
    public <T extends Response> void unbind(ICallback<T> callback)
    {
        Map<BasicService, List<CustomServiceConnection>> servicesToUnbind = new HashMap<>();

        for(BasicService service : services.keySet())
        {
            List<CustomServiceConnection> unbindingConnections = new LinkedList<>();
            List<CustomServiceConnection> connections = services.get(service);

            if (connections != null)
            {
                for(CustomServiceConnection connection : connections)
                {
                    if (connection.callback.equals(callback))
                    {
                        unbindingConnections.add(connection);
                    }
                }

                servicesToUnbind.put(service, unbindingConnections);
            }
        }

        for (BasicService service : servicesToUnbind.keySet())
        {
            List<CustomServiceConnection> connections = services.get(service);
            List<CustomServiceConnection> unbindingConnections = servicesToUnbind.get(service);

            if(connections!=null && unbindingConnections != null)
            {
                for (CustomServiceConnection connection : unbindingConnections)
                {
                    service.removeCallback(connection.callback);
                    connections.remove(connection);
                    context.unbindService(connection);
                }

                if(connections.size() == 0)
                    services.remove(service);
                else
                    services.put(service, connections);
            }
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
        public void onServiceConnected(ComponentName name, IBinder binder)
        {
            BasicService<T> service = ((LocalBinder) binder).getService();
            List<CustomServiceConnection> existingConnections = services.get(service);

            waitingServices.remove(service.getClass());

            if(existingConnections == null)
                existingConnections = new LinkedList<>();

            service.addCallback(callback);
            existingConnections.add(this);
            services.put(service,existingConnections);

            if(service instanceof DatabaseService)
                ((DatabaseService) service).onConnected();

            if (waitingRequests.containsKey(service.getClass()))
            {
                Map<Request,ICallback> requestList = waitingRequests.get(service.getClass());

                if (requestList != null)
                {
                    for (Request req : requestList.keySet())
                    {
                        postRequest(service.getClass(), req, requestList.get(req));
                    }
                }
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onServiceDisconnected(ComponentName name)
        {
            Log.e("onServiceDisconnected", callback.getClass().getSimpleName() + " asdadasdadsa");
            for(BasicService service : services.keySet())
            {
                List<CustomServiceConnection> connections = services.get(service);

                if(connections != null)
                {
                    for (CustomServiceConnection connection : connections)
                    {
                        if (connection.equals(this))
                        {
                            service.removeCallback(callback);
                            connections.remove(this);
                            context.unbindService(connection);
                        }
                    }
                    if(connections.size() == 0)
                        services.remove(service);
                    else
                        services.put(service, connections);
                }
            }
        }
    }

    private BasicService getServiceByClass(Class c)
    {
        for(BasicService service : services.keySet())
        {
            if (service.getClass().equals(c))
                return service;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public void postRequest(Class serviceClass, Request request, ICallback callback)
    {
        BasicService service = getServiceByClass(serviceClass);

        if(service == null || waitingServices.contains(service.getClass()))
        {
            Map<Request,ICallback> requestList = waitingRequests.get(serviceClass);

            if (requestList == null)
            {
                requestList = new HashMap<>();
                waitingRequests.put(serviceClass, requestList);
            }

            requestList.put(request, callback);
            bind(serviceClass, callback);
        }
        else if (service instanceof DatabaseService)
        {
            try
            {
                if (request instanceof CreateRequest)
                    ((DatabaseService) service).postRequest((CreateRequest) request, callback);
                if (request instanceof DeleteRequest)
                    ((DatabaseService) service).postRequest((DeleteRequest) request, callback);
                if (request instanceof EditRequest)
                    ((DatabaseService) service).postRequest((EditRequest) request, callback);
                if (request instanceof GetAllRequest)
                    ((DatabaseService) service).postRequest((GetAllRequest) request, callback);
                if (request instanceof GetByIdRequest)
                    ((DatabaseService) service).postRequest((GetByIdRequest) request, callback);
            }
            catch (Exception e)
            {
                Toast.makeText(context, "An error occured!\n Error code 100", Toast.LENGTH_LONG).show();
                Log.e("Error code 100", e.getLocalizedMessage());
            }
        }
        else if (service instanceof LoginService && request instanceof LoginRequest)
            ((LoginService) service).postRequest((LoginRequest)request, callback);
        else if (service instanceof CredentialsCheckService && request instanceof CredentialsRequest)
            ((CredentialsCheckService) service).postRequest((CredentialsRequest)request, callback);
    }
}
