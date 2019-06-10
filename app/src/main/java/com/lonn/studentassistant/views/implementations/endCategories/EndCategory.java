package com.lonn.studentassistant.views.implementations.endCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.activities.abstractions.IEntityActivity;
import com.lonn.studentassistant.common.abstractions.EntityManager;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;
import com.lonn.studentassistant.views.entityViews.EntityView;

import java.util.LinkedList;

public class EndCategory<T extends BaseEntity> extends ScrollViewCategory<T> implements IEntityActivity<T>
{
    private String viewType;
    private EntityManager<T> entityManager;

    public EndCategory(Context context, String viewType)
    {
        super(context);
        this.viewType = viewType;
    }

    public EndCategory(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public EndCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EndCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(Context context)
    {
        super.init(context);
        entityManager = new EntityManager<>(categoryContentLayout, new LinkedList<String>(), this);
    }

    public void addOrUpdate(T entity)
    {
        entityManager.addOrUpdate(entity);
    }

    public void delete(T entity)
    {
        entityManager.delete(entity);
    }

    public EntityView<T> getEntityViewInstance(T entity)
    {
        return new EntityView<>(getContext(), entity, viewType);
    }
}
