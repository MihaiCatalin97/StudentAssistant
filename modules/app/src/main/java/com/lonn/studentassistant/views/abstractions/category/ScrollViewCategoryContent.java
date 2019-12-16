package com.lonn.studentassistant.views.abstractions.category;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.views.EntityViewType;
import com.lonn.studentassistant.views.abstractions.ScrollViewItem;
import com.lonn.studentassistant.views.implementations.EntityView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScrollViewCategoryContent<T extends BaseEntity> extends LinearLayout {
    protected Map<String, EntityView<T>> childEntityViews = new HashMap<>();
    protected Map<String, ScrollViewCategory<T>> subcategoryViews = new HashMap<>();

    public ScrollViewCategoryContent(Context context) {
        super(context);
    }

    public ScrollViewCategoryContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void sortChildren() {
        String[] categories = new String[childEntityViews.size()];
        categories = childEntityViews.keySet().toArray(categories);
        Arrays.sort(categories);

        for (String childCategory : categories) {
            View child = childEntityViews.get(childCategory);

            if (child != null) {
                child.bringToFront();
            }
        }

        findViewById(R.id.layoutCategoryAdd).bringToFront();
    }

    public void setOnAddTap(Runnable runnable) {
        findViewById(R.id.layoutCategoryAdd).setOnClickListener(v -> runnable.run());
    }

    public void addEntity(T entity, EntityViewType viewType, int permissionLevel) {
        EntityView<T> newEntityView = new EntityView<>(getContext(),
                entity,
                viewType,
                permissionLevel);

        childEntityViews.put(entity.getKey(),
                newEntityView);

        addChildView(newEntityView);
    }

    public void addSubcategory(ScrollViewCategory<T> subCategory) {
        subcategoryViews.put(subCategory.getViewModel().getCategoryTitle(),
                subCategory);

        addChildView(subCategory);
    }

    public void removeAllEntities() {
        for (EntityView<T> entityView : childEntityViews.values()) {
            removeView(entityView);
        }

        childEntityViews.clear();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() == 0) {
            index = 0;
        }
        else {
            index = getChildCount() - 1;
        }

        super.addView(child, index, params);
    }

    private void addChildView(ScrollViewItem<T> childView) {
        addView(childView);
    }
}
