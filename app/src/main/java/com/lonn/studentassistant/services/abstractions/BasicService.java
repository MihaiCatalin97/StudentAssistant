package com.lonn.studentassistant.common.abstractClasses;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.lonn.studentassistant.common.LocalBinder;
import com.lonn.studentassistant.common.requests.Request;
import com.lonn.studentassistant.common.responses.Response;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicService extends Service
{
    private final IBinder binder = new LocalBinder<>(this);
    protected List<IServiceCallback> serviceCallbacks = new ArrayList<>(0);

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void addCallback(IServiceCallback callback) {
        serviceCallbacks.add(callback);
    }

    public void removeCallback(IServiceCallback callback)
    {
        serviceCallbacks.remove(callback);
    }

    public abstract void postRequest(Request req);

    protected void sendResponse(Response response)
    {
        for (IServiceCallback callback : serviceCallbacks)
        {
            Log.e("Sending response " + response.action, response.result);
            callback.processResponse(response);
        }
    }
}
