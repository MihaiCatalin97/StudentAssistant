package com.lonn.studentassistant.views.abstractions.category;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.databinding.CategoryLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.viewModels.entities.CategoryViewModel;
import com.lonn.studentassistant.views.abstractions.ExpandAnimation;
import com.lonn.studentassistant.views.abstractions.ScrollViewItem;

import java.util.Collection;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static com.lonn.studentassistant.R.id.arrowCategory;
import static com.lonn.studentassistant.R.id.layoutCategoryContent;
import static com.lonn.studentassistant.R.id.layoutCategoryHeader;
import static com.lonn.studentassistant.R.id.layoutCategoryMain;
import static com.lonn.studentassistant.R.layout.category_layout;

public class ScrollViewCategory<T extends BaseEntity> extends ScrollViewItem<T> {
    protected CategoryViewModel<T> viewModel = new CategoryViewModel<>();
    protected ScrollViewCategoryHeader header;
    protected ScrollViewCategoryContent<T> content;

    protected boolean expanded = false;

    public ScrollViewCategory(Context context) {
        super(context);
        init(context);
    }

    public ScrollViewCategory(Context context, CategoryViewModel<T> viewModel) {
        super(context);
        this.viewModel = viewModel;
        init(context);
    }

    public ScrollViewCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollViewCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CategoryViewModel<T> getViewModel() {
        return viewModel;
    }

    public boolean containsChildCategoryWithName(String categoryName) {
        for (CategoryViewModel category : viewModel.getChildCategories()) {
            if (category.getCategoryTitle().equals(categoryName)) {
                return true;
            }
        }

        return false;
    }

    public void addChildCategory(ScrollViewCategory<T> category) {
        addView(category);
    }

    public ScrollViewCategoryContent<T> getContent() {
        return content;
    }

    public void addEntities(List<T> entities) {
        if (getViewModel().isEndCategory()) {
            for (T entity : entities) {
                viewModel.addEntity(entity);
                getContent().addEntity(entity,
                        viewModel.getViewType(),
                        viewModel.getPermissionLevel());
            }
        }
        else {
            for (CategoryViewModel<T> subcategory : getSubCategories()) {
                for (T entity : entities) {
                    if (subcategory.getShouldContain().test(entity)) {
                        subcategory.addEntity(entity);
                    }
                }
            }
        }
    }

    public Collection<CategoryViewModel<T>> getSubCategories() {
        return viewModel.getChildCategories();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof ScrollViewItem) {
            getContent().addView(child, index, params);
        }
        else {
            super.addView(child, index, params);
        }

        if (header != null) {
            header.bringToFront();
        }
    }

    protected void initContent() {
        content = findViewById(layoutCategoryMain).findViewById(layoutCategoryContent);
        header = findViewById(layoutCategoryMain).findViewById(layoutCategoryHeader);

        header.setOnClickListener(v -> {
            expanded = !expanded;
            animateExpand();
        });

        if (getChildCount() == 1 && !viewModel.isShowEmpty()) {
            setVisibility(View.GONE);
        }
    }

    @Override
    protected void inflateLayout(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            CategoryLayoutBinding binding = DataBindingUtil.inflate(
                    layoutInflater,
                    category_layout,
                    this,
                    true);
            binding.setCategoryModel(viewModel);
        }
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        initContent();
    }

    private void animateExpand() {
        RotateAnimation animation =
                new RotateAnimation(expanded ? 0 : 180, expanded ? 180 : 360,
                        RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setFillBefore(true);

        header.findViewById(arrowCategory).startAnimation(animation);

        ExpandAnimation expandAnimation = new ExpandAnimation();
        expandAnimation.setDuration(500);
        expandAnimation.start(content);
    }
}
