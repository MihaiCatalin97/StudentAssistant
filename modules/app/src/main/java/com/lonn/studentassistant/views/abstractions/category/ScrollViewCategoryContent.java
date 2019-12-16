package com.lonn.studentassistant.views.abstractions.category;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.views.EntityViewType;
import com.lonn.studentassistant.views.implementations.EntityView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScrollViewCategoryContent<T extends BaseEntity> extends LinearLayout {
    protected Map<String, EntityView<T>> childEntityViews = new HashMap<>();
    protected View categoryAddLayout;

    public ScrollViewCategoryContent(Context context) {
        super(context);
        initContent();
    }

    public ScrollViewCategoryContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContent();
    }

    public ScrollViewCategoryContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContent();
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

        addView(newEntityView);

        childEntityViews.put(entity.getKey(),
                newEntityView);
    }

    @Override
    public void addView(View view) {
        super.addView(view);

        if (view != categoryAddLayout) {
            view.bringToFront();
        }
    }

    public void removeAllEntities() {
        for (EntityView<T> entityView : childEntityViews.values()) {
            removeView(entityView);
        }

        childEntityViews.clear();
    }

    protected void initContent() {
        categoryAddLayout = findViewById(R.id.layoutCategoryAdd);
        categoryAddLayout.bringToFront();
    }
}
