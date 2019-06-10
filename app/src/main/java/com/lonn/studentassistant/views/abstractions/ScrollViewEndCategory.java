package com.lonn.studentassistant.views.abstractions;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.activities.abstractions.IEntityActivity;
import com.lonn.studentassistant.common.abstractions.EntityManager;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.LinkedList;

public abstract class ScrollViewEndCategory<T extends BaseEntity> extends ScrollViewCategory<T> implements IEntityActivity<T>
{
    private EntityManager<T> entityManager;

    public ScrollViewEndCategory(Context context)
    {
        super(context);
    }

    public ScrollViewEndCategory(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public ScrollViewEndCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollViewEndCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

}
