package com.lonn.studentassistant.common.interfaces;

import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.entities.lists.CustomList;

import java.util.List;

public interface IDatabaseController<T extends BaseEntity>
{
    void setAll(CustomList<T> items);
    void update(T item);
    void update(CustomList<T> items);
    void add(T item);
    void add(CustomList<T> items);
    void remove(T item);
    void remove(CustomList<T> item);
}
