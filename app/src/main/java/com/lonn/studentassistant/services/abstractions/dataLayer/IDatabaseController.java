package com.lonn.studentassistant.services.abstractions.dataLayer;

import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.services.abstractions.DatabaseService;

import java.util.List;

public interface IDatabaseController<T extends BaseEntity>
{
    Class getType();
    void populateRepository(List<T> list, String child);
    void populateRepository(List<T> list);
    void update(T item);
    void update(List<T> items);
    void add(T item);
    void add(List<T> items);
    void remove(T item);
    void remove(List<T> item);
    void bindService(DatabaseService<T> service);
    void unbindService(DatabaseService<T> service);
}
