package com.lonn.studentassistant.services.abstractions;

import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.callbacks.IDatabaseCallback;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.requests.CreateRequest;
import com.lonn.studentassistant.common.requests.DeleteRequest;
import com.lonn.studentassistant.common.requests.GetAllRequest;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.activities.abstractions.callbacks.ICallback;
import com.lonn.studentassistant.common.requests.EditRequest;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.notifications.abstractions.NotificationCreator;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class DatabaseService<T extends BaseEntity> extends BasicService<DatabaseResponse<T>> implements IDatabaseService<T>
{
    protected NotificationCreator<T> notificationCreator;
    protected AbstractRepository<T> repository;

    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseApp.initializeApp(getBaseContext());

        if (notificationCreator == null)
            notificationCreator = instantiateCreator();
        if (repository == null)
            repository = instantiateRepository();
        Log.e("Service Creating", this.getClass().getSimpleName());
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId)
    {
        Log.e("Start command", this.getClass().getSimpleName());


        /*Notification notification = new NotificationCompat.Builder(getBaseContext(), "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Database Service")
                .setContentText(this.getClass().getSimpleName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        startForeground(0, notification);
        */

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent intent)
    {
        super.onTaskRemoved(intent);
        handleDestroy();
    }

    @Override
    public void onDestroy()
    {
        handleDestroy();
        super.onDestroy();
    }

    public void addCallback(ICallback<DatabaseResponse<T>> callback) {
        super.addCallback(callback);
    }

    public void sendResponse(CreateResponse<T> response, ICallback<DatabaseResponse<T>> callback)
    {
        if (serviceCallbacks.size() == 0)
        {
            if(notificationCreator != null)
                notificationCreator.showNotification(this, response);
        }
        else
        {
            if (callback != null)
                ((IDatabaseCallback<T>) callback).processResponse(response);
            else
                for(ICallback<DatabaseResponse<T>> callback1 : serviceCallbacks)
                    ((IDatabaseCallback<T>) callback1).processResponse(response);
        }
    }

    public void sendResponse(DeleteResponse<T> response, ICallback<DatabaseResponse<T>> callback)
    {
        if (serviceCallbacks.size() == 0)
        {
            if(notificationCreator != null)
                notificationCreator.showNotification(this, response);
        }
        else
        {
            if (callback != null)
                ((IDatabaseCallback<T>) callback).processResponse(response);
            else
                for(ICallback<DatabaseResponse<T>> callback1 : serviceCallbacks)
                    ((IDatabaseCallback<T>) callback1).processResponse(response);
        }
    }

    public void sendResponse(EditResponse<T> response, ICallback<DatabaseResponse<T>> callback)
    {
        if (serviceCallbacks.size() == 0)
        {
            if(notificationCreator != null)
                notificationCreator.showNotification(this, response);
        }
        else
        {
            if (callback != null)
                ((IDatabaseCallback<T>) callback).processResponse(response);
            else
                for(ICallback<DatabaseResponse<T>> callback1 : serviceCallbacks)
                    ((IDatabaseCallback<T>) callback1).processResponse(response);
        }
    }

    public void sendResponse(GetAllResponse<T> response, ICallback<DatabaseResponse<T>> callback)
    {
        if (serviceCallbacks.size() > 0)
        {
            ((IDatabaseCallback<T>)callback).processResponse(response);
        }
        else
        {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "id")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Get all updated")
                    .setContentText("Items received " + Integer.toString(response.getItems().size()))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            notificationManager.notify(new Random().nextInt(50), builder.build());
        }
    }

    public void sendResponse(GetByIdResponse<T> response, ICallback<DatabaseResponse<T>> callback)
    {
        ((IDatabaseCallback<T>)callback).processResponse(response);
    }

    public void postRequest(GetAllRequest<T> request, ICallback<DatabaseResponse<T>> callback)
    {
        List<T> result = repository.getAll();

        if (result == null || result.size() == 0)
        {
            repository.populateRepository(callback);
        }
        else
        {
            sendResponse(new GetAllResponse<>("success", new ArrayList<>(result)), callback);
        }
    }

    public void postRequest(GetByIdRequest<T> request, ICallback<DatabaseResponse<T>> callback)
    {
        T result = repository.getById(request.getKey());

        if(result == null)
        {
            Log.e("Populating repository", "GetById");
            repository.populateRepository(request.getKey(), callback);
        }
        else
        {
            Log.e("Sending response", "GetById");
            sendResponse(new GetByIdResponse<>("success", result), callback);
        }
    }

    public void postRequest(EditRequest<T> request, ICallback<DatabaseResponse<T>> callback)
    {
        repository.update(request.getItems());
        sendResponse(new EditResponse<>("success", request.getItems()), callback);
    }

    public void postRequest(CreateRequest<T> request, ICallback<DatabaseResponse<T>> callback)
    {
        repository.add(request.getItems());
        sendResponse(new CreateResponse<>("success", request.getItems()), callback);
    }

    public void postRequest(DeleteRequest<T> request, ICallback<DatabaseResponse<T>> callback)
    {
        repository.remove(request.getItems());
        sendResponse(new DeleteResponse<>("success", request.getItems()), callback);
    }

    public abstract void onConnected();
    protected abstract void handleDestroy();
    protected abstract AbstractRepository<T> instantiateRepository();
    protected abstract NotificationCreator<T> instantiateCreator();
}
