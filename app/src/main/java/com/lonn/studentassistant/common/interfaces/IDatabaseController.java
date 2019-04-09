package com.lonn.studentassistant.common.interfaces;

import java.util.List;

public interface IDatabaseController<T>
{
    void setAll(List<T> items);
    void update(T item);
    void update(List<T> items);
    void add(T item);
    void add(List<T> items);
    void remove(T item);
    void remove(List<T> item);
}
