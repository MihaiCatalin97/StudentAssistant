package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.entities.BaseEntity;

public interface IBusinessLayer<T extends BaseEntity>
{
    void addEntityToList(BaseEntity entity);
    void removeEntityFromList(BaseEntity entity);
    void editActivityEntity(T entity);
    void unbindServices();
    void refreshAll();
    void getDialogEntities(String dialogType);
    boolean containsReferenceToEntity(String key);
}
