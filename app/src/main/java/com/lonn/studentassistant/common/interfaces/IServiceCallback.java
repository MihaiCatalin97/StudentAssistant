package com.lonn.studentassistant.common.interfaces;

import java.util.List;

public interface IServiceCallback<T>
{
    void resultGetById(T item);
    void resultGetAll(List<T> items);
}
