package com.lonn.studentassistant.services.abstractions;

import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
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
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseService<T extends BaseEntity> extends BasicService<DatabaseResponse<T>> implements IDatabaseService<T>
{
    protected AbstractRepository<T> repository;

    public void addCallback(ICallback<DatabaseResponse<T>> callback) {
        super.addCallback(callback);

        if (repository == null)
        {
            repository = instantiateRepository();
        }
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
        for (ICallback<DatabaseResponse<T>> callback : serviceCallbacks)
        {
            ((IDatabaseCallback<T>)callback).processResponse(response);
        }
    }

    public void sendResponse(GetAllResponse<T> response)
    {
        for (ICallback<DatabaseResponse<T>> callback : serviceCallbacks)
        {
            ((IDatabaseCallback<T>)callback).processResponse(response);
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
}
