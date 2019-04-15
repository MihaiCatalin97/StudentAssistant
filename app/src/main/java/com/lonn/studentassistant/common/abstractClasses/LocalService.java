package com.lonn.studentassistant.common.abstractClasses;

import com.lonn.studentassistant.common.requests.DatabaseRequest;
import com.lonn.studentassistant.common.requests.Request;
import com.lonn.studentassistant.common.interfaces.IService;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;
import com.lonn.studentassistant.common.responses.Response;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class LocalService<T extends BaseEntity> extends BasicService implements IService
{
    protected AbstractRepository<T> repository;

    public void addCallback(IServiceCallback callback) {
        super.addCallback(callback);

        if (repository == null)
        {
            repository = instantiateRepository();
        }
    }

    public void postRequest(Request req)
    {
        T item;
        if (repository.getAll() == null)
            return;

        if (!(req instanceof DatabaseRequest) && !req.action.equals("getAll"))
            return;

        if (req.action.equals("getAll"))
        {
            processGetAll(req);
        }
        else if (req.action.equals("getById"))
        {
            processGetById((DatabaseRequest)req);
        }
        else if (req.action.equals("delete"))
        {
            item = (T)((DatabaseRequest)req).item;

            if (repository.getAll().contains(item))
            {
                respondOneItem(req.action,"success", item);
                repository.remove(item);
            }
            else
            {
                respondOneItem(req.action,repository.getType().getSimpleName() + " not found", item);
            }
        }
    }

    public void processGetAll(Request req)
    {
        if (repository.getAll() == null || repository.getAll().size() == 0)
        {
            repository.populateRepository();
        }
        else
        {
            respondMultipleItems(req.action,"success", new ArrayList<T>(repository.getAll()));
        }
    }

    public void processGetById(DatabaseRequest req)
    {
        if(repository.getById(req.key) == null)
        {
            repository.populateRepository(req.key);
        }
        else
        {
            respondOneItem(req.action, "success", repository.getById(req.key));
        }
    }

    public void respondMultipleItems(String action, String result, List<T> items)
    {
        Response<T> response = new Response<>(repository.getType(), action, result, items);
        sendResponse(response);
    }

    public void respondOneItem(String action, String result, final T item)
    {
        Response<T> response = new Response<>(repository.getType(), action, result, null);

        response.items = new ArrayList<T>()
        {{
            add(item);
        }};

        sendResponse(response);
    }

    protected abstract AbstractRepository<T> instantiateRepository();
}
