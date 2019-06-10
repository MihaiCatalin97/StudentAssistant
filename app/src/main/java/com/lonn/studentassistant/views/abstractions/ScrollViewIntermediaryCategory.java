package com.lonn.studentassistant.views.abstractions;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.entities.BaseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ScrollViewIntermediaryCategory<T extends BaseEntity> extends ScrollViewCategory<T>
{
    private Map<String, ScrollViewCategory<T>> subCategories = new HashMap<>();

    public ScrollViewIntermediaryCategory(Context context)
    {
        super(context);
    }

    public ScrollViewIntermediaryCategory(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public ScrollViewIntermediaryCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollViewIntermediaryCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected abstract List<String> getEntityNextCategories(T entity);

    public void addOrUpdate(T entity)
    {
        for(String categoryString : getEntityNextCategories(entity))
        {
            ScrollViewCategory<T> category = subCategories.get(categoryString);

            if (category != null)
            {
                category.addOrUpdate(entity);
            }
            else
            {
                category = getSubCategoryInstance(getContext());

                categoryContentLayout.addView(category);

                category.setCategory(categoryString);
                category.addOrUpdate(entity);

                subCategories.put(categoryString, category);
            }
        }
    }

    public void delete(T entity)
    {
        for (String categoryString : getEntityNextCategories(entity))
        {
            ScrollViewCategory<T> category = subCategories.get(categoryString);

            if (category != null)
                category.delete(entity);
        }
    }

    public abstract ScrollViewCategory<T> getSubCategoryInstance(Context context);
}
