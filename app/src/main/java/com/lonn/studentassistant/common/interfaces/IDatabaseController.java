package com.lonn.studentassistant.common.interfaces;

import java.util.List;
import java.util.UUID;

public interface IDatabaseController<T>
{
    void setAll(List<T> items);
    void update(T item);
    void add(T item);
    void remove(T item);
}
