package com.lonn.studentassistant.viewModels;

import android.databinding.BaseObservable;

import com.lonn.studentassistant.entities.BaseEntity;

public abstract class EntityViewModel<T extends BaseEntity> extends BaseObservable
{
    public abstract void update(T entity);
}
