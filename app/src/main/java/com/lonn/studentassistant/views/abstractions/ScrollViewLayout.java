package com.lonn.studentassistant.views.abstractions;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ScrollViewLayout<T extends BaseEntity> extends ScrollViewItem<T>
{
    private Map<String, ScrollViewCategory<T>> categories;

    protected LinearLayout categoryContentLayout;

    public ScrollViewLayout(Context context)
    {
        super(context);
        init(context);
    }

    public ScrollViewLayout(Context context, AttributeSet set)
    {
        super(context, set);
        init(context);
    }

    public ScrollViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollViewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void inflateLayout(Context context)
    {
        inflate(context, R.layout.scrollview_layout, this);
        categoryContentLayout = findViewById(R.id.layoutCategoryContent);
        categories = new HashMap<>();
    }

    public void addOrUpdate(T entity)
    {
        for(String categoryString : getEntityNextCategory(entity))
        {
            ScrollViewCategory<T> category = categories.get(categoryString);

            if (category != null)
            {
                category.addOrUpdate(entity);
            }
            else
            {
                category = getBaseCategoryInstance(getContext());
                category.setCategory(categoryString);
                category.addOrUpdate(entity);
                categoryContentLayout.addView(category);

                categories.put(categoryString, category);

                sortCategories();
            }
        }
    }

    private void sortCategories()
    {
        String[] categoriesArray = new String[categories.size()];
        categoriesArray = categories.keySet().toArray(categoriesArray);

        Arrays.sort(categoriesArray);

        for(String category : categoriesArray)
        {
            View categoryView = categories.get(category);

            if(categoryView != null)
                categoryView.bringToFront();
        }
    }

    public void delete(T entity)
    {
        for(String categoryString : getEntityNextCategory(entity))
        {
            ScrollViewCategory<T> category = categories.get(categoryString);

            if (category != null)
                category.delete(entity);
        }
    }

    protected abstract List<String> getEntityNextCategory(T entity);

    protected abstract ScrollViewCategory<T> getBaseCategoryInstance(Context context);
}
