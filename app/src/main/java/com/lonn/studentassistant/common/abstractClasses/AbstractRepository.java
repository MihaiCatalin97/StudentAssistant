package com.lonn.studentassistant.common.abstractClasses;

import com.lonn.studentassistant.common.interfaces.IDatabaseController;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.entities.lists.CustomList;

public abstract class AbstractRepository<T extends BaseEntity>
{
    private IDatabaseController<T> databaseController;
    private CustomList<T> items;

    protected AbstractRepository(IDatabaseController<T> databaseController)
    {
        items = new CustomList<>();
        this.databaseController = databaseController;
        reload();
    }

    private void reload()
    {
        items.clear();
        databaseController.setAll(items);
    }

    private T getById(String id)
    {
        for (T u : items)
        {
            if (u.getKey().equals(id))
                return u;
        }

        return null;
    }

    public CustomList<T> getAll()
    {
        return items;
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

    public void update(CustomList<T> items)
    {
        CustomList<T> resultList = new CustomList<>(null);

        for (T item : items)
        {
            T updatingItem = getById(item.getKey());

            if (updatingItem != null)
            {
                this.items.set(this.items.indexOf(updatingItem), item);
                resultList.add(item);
            }
        }

        databaseController.add(resultList);
    }

    public void add(T item)
    {
        if (!items.contains(item)) {
            items.add(item);
            databaseController.add(item);
        }
    }

    public void add(CustomList<T> items)
    {
        CustomList<T> resultList = new CustomList<>();

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

    public void remove(CustomList<T> items)
    {
        CustomList<T> resultList = new CustomList<>();

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
