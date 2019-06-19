package com.lonn.studentassistant.services.abstractions.dataLayer;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.entities.CustomList;
import com.lonn.studentassistant.services.abstractions.DatabaseService;
import com.lonn.studentassistant.services.implementations.coursesService.CourseService;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepository<T extends BaseEntity>
{
    protected IDatabaseController<T> databaseController;
    private CustomList<T> items;

    protected AbstractRepository(IDatabaseController<T> databaseController)
    {
        items = new CustomList<>();
        this.databaseController = databaseController;
    }

    public void populateRepository(ICallback<DatabaseResponse<T>> callback)
    {
        databaseController.populateRepository(items, callback);
    }

    public void populateRepository(String child, ICallback<DatabaseResponse<T>> callback)
    {
        databaseController.populateRepository(items, child, callback);
    }

    public T getById(String id)
    {
        for (T u : items)
        {
            if (u.getKey().equals(id))
                return u;
        }

        return null;
    }

    public List<T> getAll()
    {
        return new ArrayList<>(items);
    }

    public void update(T item)
    {
        T updatingItem = getById(item.getKey());

        if (updatingItem != null)
        {
            items.set(items.indexOf(updatingItem), item);
            databaseController.update(item);
        }
    }

    public void update(List<T> items)
    {
        List<T> resultList = new ArrayList<>();

        for (T item : items)
        {
            T updatingItem = getById(item.getKey());

            if (updatingItem != null)
            {
                this.items.set(this.items.indexOf(updatingItem), item);
                resultList.add(item);
            }
        }

        Log.e("Enrolling courses 2", Integer.toString(resultList.size()));
        databaseController.update(resultList);
    }

    public void add(T item)
    {
        if (!items.contains(item)) {
            items.add(item);
            databaseController.add(item);
        }
    }

    public void add(List<T> items)
    {
        List<T> resultList = new ArrayList<>();

        for (T item : items)
        {
            if (!this.items.contains(item))
            {
                this.items.add(item);
                resultList.add(item);
            }
        }

        databaseController.add(resultList);
    }

    public void remove(T item)
    {
        if (items.contains(item))
        {
            items.remove(item);
            databaseController.remove(item);
        }
    }

    public void remove(List<T> items)
    {
        List<T> resultList = new ArrayList<>();

        for (T item : items)
        {
            if (this.items.contains(item))
            {
                this.items.remove(item);
                resultList.add(item);
            }
        }

        databaseController.remove(resultList);
    }
}
