package com.lonn.studentassistant.views.abstractions;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import android.os.Build;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.CategoryLayoutBinding;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.viewModels.CategoryViewModel;
import com.lonn.studentassistant.views.implementations.EntityView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class ScrollViewCategory<T extends BaseEntity> extends ScrollViewItem<T>
{
    protected CategoryViewModel categoryViewModel = new CategoryViewModel();
    protected View categoryHeaderLayout;
    protected LinearLayout categoryContentLayout;
    protected RelativeLayout categoryMainLayout;
    protected ConstraintLayout categoryAddLayout;

    protected String viewType = "full";
    protected String generateChildCategories = "none";

    protected boolean expanded = false;
    protected boolean showEmpty = false;
    protected boolean isEndCategory = true;

    protected Map<String, ScrollViewItem<T>> children = new HashMap<>();
    protected List<T> listAllEntities = new LinkedList<>();

    public ScrollViewCategory(Context context)
    {
        super(context);
        init(context);
    }

    public ScrollViewCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        getAttributesFromSet(attrs);
        init(context);
    }

    public ScrollViewCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributesFromSet(attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollViewCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getAttributesFromSet(attrs);
        init(context);
    }

    protected abstract void initCategoryViewModel();

    public void setCategory(final String category)
    {
        categoryViewModel.category = category;
        categoryViewModel.notifyPropertyChanged(BR.category);
    }

    @Override
    protected void inflateLayout(Context context)
    {
        CategoryLayoutBinding binding = DataBindingUtil.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), R.layout.category_layout, this, true);
        binding.setCategoryModel(categoryViewModel);
    }

    @Override
    protected void init(Context context)
    {
        super.init(context);
        initContent();
        initCategoryViewModel();
    }

    protected void initContent()
    {
        categoryMainLayout = findViewById(R.id.layoutCategoryMain);
        categoryContentLayout = categoryMainLayout.findViewById(R.id.layoutCategoryContent);
        categoryHeaderLayout = categoryMainLayout.findViewById(R.id.layoutCategoryHeader);
        categoryAddLayout = categoryContentLayout.findViewById(R.id.layoutCategoryAdd);

        categoryHeaderLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                expanded = !expanded;

                animateExpand();
            }
        });

        if(categoryContentLayout.getChildCount() == 1 && !showEmpty)
            setVisibility(View.GONE);
    }

    private void getAttributesFromSet(AttributeSet set)
    {
        TypedArray a = getContext().obtainStyledAttributes(set, R.styleable.ScrollViewCategory );
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.ScrollViewCategory_category_title:
                    setCategory(a.getString(attr));
                    break;
                case R.styleable.ScrollViewCategory_view_type:
                    viewType = a.getString(attr);
                    break;
                case R.styleable.ScrollViewCategory_show_empty:
                    showEmpty = a.getBoolean(attr, false);
                    break;
                case R.styleable.ScrollViewCategory_generate_child_categories:
                    generateChildCategories = a.getString(attr);

                    if(generateChildCategories != null)
                        isEndCategory = generateChildCategories.equals("none");
                    break;
                case R.styleable.ScrollViewCategory_show_base_header:
                    categoryViewModel.showHeader = a.getBoolean(attr, true);
                    break;
                case R.styleable.ScrollViewCategory_permission_level:
                    categoryViewModel.permissionLevel = a.getInteger(attr, 0);
                    break;
            }
        }
        a.recycle();
    }

    private void animateExpand()
    {
        RotateAnimation animation =
                new RotateAnimation(expanded?0:180,expanded?180:360,
                        Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setFillBefore(true);

        categoryHeaderLayout.findViewById(R.id.arrowCategory).startAnimation(animation);

        ExpandAnimation expandAnimation = new ExpandAnimation();
        expandAnimation.setDuration(500);
        expandAnimation.start(categoryContentLayout);
    }

    public void addOrUpdate(T entity)
    {
        if(shouldContain(entity))
        {
            if(isEndCategory)
            {
                ScrollViewItem<T> view = children.get(entity.getKey());

                if(view != null)
                {
                    view.addOrUpdate(entity);
                }
                else
                {
                    view = new EntityView<>(getContext(), entity, viewType, categoryViewModel.permissionLevel);
                    children.put(entity.getKey(), view);
                    addView(view);
                }
            }
            else
            {
                generateChildCategories(entity);
                setCategoriesViewType();

                for (String category : children.keySet())
                {
                    ScrollViewItem<T> child = children.get(category);

                    if (child != null)
                        child.addOrUpdate(entity);
                }
            }
        }

        updateEntitiesList(entity);
    }

    private void updateEntitiesList(T entity)
    {
            for(T listEntity : listAllEntities)
            {
                if(listEntity.getKey().equals(entity.getKey()))
                {
                    listAllEntities.remove(listEntity);
                    break;
                }
            }
        if(!shouldContain(entity))
        {
            listAllEntities.add(entity);
        }
    }

    public void delete(T entity)
    {
        if(isEndCategory)
        {
            ScrollViewItem<T> view = children.get(entity.getKey());

            if(view != null)
            {
                children.remove(entity.getKey());
                categoryContentLayout.removeView(view);
            }
        }
        else
        {
            for (String category : children.keySet())
            {
                ScrollViewItem<T> child = children.get(category);

                if (child != null)
                    child.delete(entity);
            }
        }
        updateEntitiesList(entity);
    }

    public void delete(String key)
    {
        if(isEndCategory)
        {
            ScrollViewItem<T> view = children.get(key);

            if(view != null)
            {
                children.remove(key);
                categoryContentLayout.removeView(view);
            }
        }
        else
        {
            for (String category : children.keySet())
            {
                ScrollViewItem<T> child = children.get(category);

                if (child != null)
                    child.delete(key);
            }
        }
    }

    @Override
    public void addView(View child)
    {
        if (getChildCount() == 0)
            super.addView(child);
        else
        {
            categoryContentLayout.addView(child);
            addChildView(child);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params)
    {
        if (getChildCount() == 0)
            super.addView(child, params);
        else
        {
            categoryContentLayout.addView(child, params);
            addChildView(child);
        }
    }

    @Override
    public void addView(View child, int index)
    {
        if (getChildCount() == 0)
            super.addView(child, index);
        else
        {
            categoryContentLayout.addView(child, index);
            addChildView(child);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params)
    {
        if (getChildCount() == 0)
            super.addView(child, index, params);
        else
        {
            categoryContentLayout.addView(child, index, params);
            addChildView(child);
        }
    }

    @Override
    public void addView(View child, int width, int height)
    {
        if(getChildCount() == 0)
            super.addView(child, width, height);
        else
        {
            categoryContentLayout.addView(child, width, height);
            addChildView(child);
        }
    }

    @SuppressWarnings("unchecked")
    private void addChildView(View v)
    {
        if (v instanceof ScrollViewCategory)
        {
            isEndCategory = false;

            ((ScrollViewCategory) v).categoryViewModel.permissionLevel = this.categoryViewModel.permissionLevel;
            ((ScrollViewCategory) v).viewType = viewType;
            ((ScrollViewCategory) v).showEmpty = showEmpty;

            children.put(((ScrollViewCategory) v).categoryViewModel.category, (ScrollViewCategory<T>) v);

            categoryAddLayout.setVisibility(View.GONE);
        }
        else if (isEndCategory && categoryViewModel.permissionLevel > 0)
        {
            categoryAddLayout.setVisibility(View.VISIBLE);
            categoryAddLayout.bringToFront();
        }
        else
            categoryAddLayout.setVisibility(View.GONE);

        this.setVisibility(View.VISIBLE);
    }

    @BindingAdapter("srcCompat")
    public static void bindSrcCompat(ImageView imageView, int resourceId)
    {
        imageView.setImageResource(resourceId);
    }

    private void setCategoriesViewType()
    {
        for(ScrollViewItem child : children.values())
        {
            if(child instanceof ScrollViewCategory)
                ((ScrollViewCategory) child).viewType = viewType;
        }
    }

    protected abstract void generateChildCategories(T entity);

    protected void sortChildren()
    {
        String[] categories = new String[children.size()];
        categories = children.keySet().toArray(categories);
        Arrays.sort(categories);

        for (String childCategory : categories)
        {
            View child = children.get(childCategory);

            if(child != null)
                child.bringToFront();
        }

        categoryAddLayout.bringToFront();
    }
}
