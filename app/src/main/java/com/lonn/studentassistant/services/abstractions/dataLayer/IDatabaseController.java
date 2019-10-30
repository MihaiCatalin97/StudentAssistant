package com.lonn.studentassistant.services.abstractions.dataLayer;

import com.lonn.studentassistant.activities.abstractions.callbacks.ICallback;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.entities.CustomList;
import com.lonn.studentassistant.services.abstractions.DatabaseService;

import java.util.List;

public interface IDatabaseController<T extends BaseEntity>
{
    Class getType();
    void populateRepository(CustomList<T> list, String child, ICallback<DatabaseResponse<T>> callback);
    void populateRepository(CustomList<T> list, ICallback<DatabaseResponse<T>> callback);
    void update(T item);
    void update(List<T> items);
    void add(T item);
    void add(List<T> items);
    void remove(T item);
    void remove(List<T> item);
    void bindService(DatabaseService<T> service);
    void unbindService(DatabaseService<T> service);
}
