package com.lonn.studentassistant.common.interfaces;

import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;
import java.util.UUID;

public abstract class IRepository<T>
{
    protected IDatabaseController<T> databaseController;
    protected List<T> items;

    protected IRepository (IDatabaseController<T> databaseController)
    {
        this.databaseController = databaseController;
    }

    public abstract T getById(Object id);
    public abstract List<T> getAll();
    public abstract void update(T item);
    public abstract void add(T item);
    public abstract void remove(T item);
}
