package com.lonn.studentassistant.common.abstractions;

import android.view.ViewGroup;

import com.lonn.studentassistant.activities.abstractions.IEntityActivity;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.views.abstractions.EntityView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EntityManager<T extends BaseEntity> implements IEntityManager<T>
{
    private ViewGroup contentLayout;
    private Map<String, EntityView<T>> entityViewMap = new HashMap<>();
    private List<String> modelEntitiesList;
    private IEntityActivity<T> activity;

    public EntityManager(ViewGroup contentLayout, List<String> modelEntitiesList, IEntityActivity<T> activity)
    {
        this.contentLayout = contentLayout;
        this.modelEntitiesList = modelEntitiesList;
        this.activity = activity;

        if (this.modelEntitiesList == null)
            this.modelEntitiesList = new LinkedList<>();
    }

    public void addOrUpdate(T entity)
    {
        EntityView<T> view = entityViewMap.get(entity.getKey());

        if(view != null)
        {
            view.update(entity);
        }
        else
        {
            view = activity.createView(entity);
            entityViewMap.put(entity.getKey(), view);
            contentLayout.addView(view);
        }

        if(!modelEntitiesList.contains(entity.getKey()))
            modelEntitiesList.add(entity.getKey());
    }

    public void delete(T entity)
    {
        EntityView<T> view = entityViewMap.get(entity.getKey());

        if(view != null)
        {
            entityViewMap.remove(entity.getKey());
            modelEntitiesList.remove(entity.getKey());
            contentLayout.removeView(view);
        }
    }
}
