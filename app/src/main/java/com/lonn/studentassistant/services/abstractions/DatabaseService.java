package com.lonn.studentassistant.services.abstractions;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.authentication.AuthenticationActivity;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.requests.CreateRequest;
import com.lonn.studentassistant.common.requests.DeleteRequest;
import com.lonn.studentassistant.common.requests.GetAllRequest;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.common.requests.EditRequest;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.notifications.abstractions.NotificationCreator;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractRepository;

import java.lang.reflect.ParameterizedType;
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

        if (notificationCreator == null)
            notificationCreator = instantiateCreator();
        if (repository == null)
            repository = instantiateRepository();
        Log.e("Service Creating", this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy()
    {
        Log.e("Service Destroying", "CourseService");
        super.onDestroy();
    }

    public void addCallback(ICallback<DatabaseResponse<T>> callback) {
        super.addCallback(callback);
    }

    public void sendResponse(CreateResponse<T> response)
    {
        for (ICallback<DatabaseResponse<T>> callback : serviceCallbacks)
        {
            ((IDatabaseCallback<T>)callback).processResponse(response);
        }
    }

    public void sendResponse(DeleteResponse<T> response)
    {
        for (ICallback<DatabaseResponse<T>> callback : serviceCallbacks)
        {
            ((IDatabaseCallback<T>)callback).processResponse(response);
        }
    }

    public void sendResponse(EditResponse<T> response)
    {
        if (serviceCallbacks.size() == 0)
        {
            if(notificationCreator != null)
                notificationCreator.showNotification(this, response);
            return;
        }

        for (ICallback<DatabaseResponse<T>> callback : serviceCallbacks)
        {
            ((IDatabaseCallback<T>)callback).processResponse(response);
        }
    }

    public void sendResponse(GetAllResponse<T> response)
    {
        if (serviceCallbacks.size() > 0)
        {
            for (ICallback<DatabaseResponse<T>> callback : serviceCallbacks)
            {
                ((IDatabaseCallback<T>)callback).processResponse(response);
            }
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

    public void sendResponse(GetByIdResponse<T> response)
    {
        for (ICallback<DatabaseResponse<T>> callback : serviceCallbacks)
        {
            ((IDatabaseCallback<T>)callback).processResponse(response);
        }
    }

    public void postRequest(GetAllRequest<T> request)
    {
        List<T> result = repository.getAll();

        if (result == null || result.size() == 0)
        {
            repository.populateRepository();
        }
        else
        {
            sendResponse(new GetAllResponse<>("success", new ArrayList<T>(result)));
        }
    }

    public void postRequest(GetByIdRequest<T> request)
    {
        T result = repository.getById(request.getKey());

        if(result == null)
        {
            repository.populateRepository(request.getKey());
        }
        else
        {
            sendResponse(new GetByIdResponse<>("success", result));
        }
    }

    public void postRequest(EditRequest<T> request)
    {
        repository.update(request.getItems());
        sendResponse(new EditResponse<>("success", request.getItems()));
    }

    public void postRequest(CreateRequest<T> request)
    {
        repository.add(request.getItems());
        sendResponse(new CreateResponse<>("success", request.getItems()));
    }

    public void postRequest(DeleteRequest<T> request)
    {
        repository.remove(request.getItems());
        sendResponse(new DeleteResponse<>("success", request.getItems()));
    }

    protected abstract AbstractRepository<T> instantiateRepository();
    protected abstract NotificationCreator<T> instantiateCreator();
}
