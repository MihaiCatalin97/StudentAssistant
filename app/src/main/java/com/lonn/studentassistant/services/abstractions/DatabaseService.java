package com.lonn.studentassistant.services.abstractions;

import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.requests.CreateRequest;
import com.lonn.studentassistant.common.requests.DeleteRequest;
import com.lonn.studentassistant.common.requests.GetAllRequest;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.activities.abstractions.IServiceCallback;
import com.lonn.studentassistant.common.requests.UpdateRequest;
import com.lonn.studentassistant.common.abstractions.Response;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class LocalService<T extends BaseEntity> extends BasicService implements IService<T>
{
    protected AbstractRepository<T> repository;

    public void addCallback(IServiceCallback callback) {
        super.addCallback(callback);

        if (repository == null)
        {
            repository = instantiateRepository();
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
            respondMultipleItems(request.action,"success", new ArrayList<>(result));
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
            respondOneItem(request.action, "success", result);
        }
    }

    public void postRequest(UpdateRequest<T> request)
    {
        repository.update(request.getItems());
        respondMultipleItems(request.action, "success", request.getItems());
    }

    public void postRequest(CreateRequest<T> request)
    {
        repository.add(request.getItems());
        respondMultipleItems(request.action, "success", request.getItems());
    }

    public void postRequest(DeleteRequest<T> request)
    {
        repository.remove(request.getItems());
        respondMultipleItems(request.action, "success", request.getItems());
    }

    public void respondMultipleItems(String action, String result, List<T> items)
    {
        DatabaseResponse<T> response = createResponse(action, result, items);
        sendResponse(response);
    }

    public void respondOneItem(String action, String result, final T item)
    {
        DatabaseResponse<T> response = createResponse(action, result, new ArrayList<T>()
        {{
            add(item);
        }});

        sendResponse(response);
    }

    protected abstract DatabaseResponse<T> createResponse(String action, String result, List<T> items);

    protected abstract AbstractRepository<T> instantiateRepository();
}
