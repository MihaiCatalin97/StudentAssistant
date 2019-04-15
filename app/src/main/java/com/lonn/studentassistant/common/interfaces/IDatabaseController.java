package com.lonn.studentassistant.common.interfaces;

import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.entities.lists.CustomList;

import java.util.List;

public interface IDatabaseController<T extends BaseEntity>
{
    Class getType();
    void populateRepository(CustomList<T> list, String child);
    void populateRepository(CustomList<T> list);
    void update(T item);
    void update(CustomList<T> items);
    void add(T item);
    void add(CustomList<T> items);
    void remove(T item);
    void remove(CustomList<T> item);
    void bindService(LocalService service);
    void unbindService(LocalService service);
}
