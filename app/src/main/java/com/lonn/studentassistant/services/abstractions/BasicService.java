package com.lonn.studentassistant.services.abstractions;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.lonn.studentassistant.common.LocalBinder;
import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.common.abstractions.Response;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicService<T extends Response> extends Service
{
    private final IBinder binder = new LocalBinder<>(this);
    protected List<ICallback<T>> serviceCallbacks = new ArrayList<>(0);

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void addCallback(ICallback<T> callback) {
        serviceCallbacks.add(callback);
    }

    public void removeCallback(ICallback<T> callback)
    {
        serviceCallbacks.remove(callback);
    }

    protected void sendResponse(T response)
    {
        Log.e("Sending response " + response.getAction(), response.getResult());

        for (ICallback<T> callback : serviceCallbacks)
        {
            callback.processResponse(response);
        }
    }
}
