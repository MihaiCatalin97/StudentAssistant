package com.lonn.studentassistant.viewModels.adapters.abstractions;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.viewModels.entities.abstractions.EntityViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class ViewModelAdapter<T extends BaseEntity, U extends EntityViewModel<T>> {
    protected FirebaseConnectedActivity firebaseConnectedActivity;

    protected ViewModelAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
        this.firebaseConnectedActivity = firebaseConnectedActivity;
    }

    public U adapt(T entity) {
        return resolveLinks(adaptOne(entity), entity);
    }

    public U adapt(T entity, boolean resolveLinks) {
        if (!resolveLinks) {
            return adaptOne(entity);
        }
        return adapt(entity);
    }

    public List<U> adapt(List<? extends T> entities) {
        List<U> adaptedEntities = new ArrayList<>();

        for (T entity : entities) {
            adaptedEntities.add(adapt(entity));
        }

        return adaptedEntities;
    }

    public List<U> adapt(List<? extends T> entities, boolean resolveLinks) {
        List<U> adaptedEntities = new ArrayList<>();

        for (T entity : entities) {
            adaptedEntities.add(adapt(entity, resolveLinks));
        }

        return adaptedEntities;
    }

    protected abstract U resolveLinks(U entityViewModel, T entity);

    protected abstract U adaptOne(T entity);
}
