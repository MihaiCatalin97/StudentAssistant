package com.lonn.studentassistant.common.abstractions;

import com.lonn.studentassistant.entities.BaseEntity;

public interface IEntityManager<T extends BaseEntity>
{
    void addOrUpdate(T entity);
    void delete(T entity);
}
