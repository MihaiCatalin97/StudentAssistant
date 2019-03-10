package com.lonn.studentassistant.common.interfaces;

import java.util.List;
import java.util.UUID;

public interface IDatabaseController<T>
{
    List<T> getAll();
    void update(T item);
    void add(T item);
    void remove(T item);
}
