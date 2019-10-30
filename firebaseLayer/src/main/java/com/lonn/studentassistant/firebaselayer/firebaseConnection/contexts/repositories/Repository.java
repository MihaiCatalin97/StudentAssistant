package com.lonn.studentassistant.firebaselayer.firebaseConnection.contexts.repositories;

import com.lonn.studentassistant.firebaselayer.models.BaseEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Repository<T extends BaseEntity> {
    T save(T entity);

    T delete(UUID id);

    T update(T entity);

    Collection<T> getAll();

    T getById(UUID id);

    List<T> filter(Map<String, String> filters);
}
