package com.lonn.studentassistant.views.abstractions.category;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
    protected boolean expanded = false;
    private CategoryViewModel<T> viewModel = new CategoryViewModel<>();
    private ScrollViewCategoryHeader header;
    private ScrollViewCategoryContent<T> content;

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

    public CategoryViewModel<T> getViewModel() {
        return viewModel;
    }

    public void addChildCategories(List<CategoryViewModel<T>> categories) {
        getViewModel().addSubcategories(categories);

        addCategoriesToContent(categories);

        hideIfEmpty();
    }

    public void addEntities(List<T> entities) {
        for (T entity : entities) {
            addEntity(entity);
        }

        hideIfEmpty();
    }

    public Collection<CategoryViewModel<T>> getSubCategories() {
        return viewModel.getChildCategories();
    }

    protected void initContent() {
        content = findViewById(layoutCategoryMain).findViewById(layoutCategoryContent);
        header = findViewById(layoutCategoryMain).findViewById(layoutCategoryHeader);

        header.setOnClickListener(v -> {
            expanded = !expanded;
            animateExpand();
        });

        header.bringToFront();
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

    private void addCategoriesToContent(List<CategoryViewModel<T>> categories) {
        for (CategoryViewModel<T> category : categories) {
            ScrollViewCategory<T> scrollViewCategory = new ScrollViewCategory<>(getContext(),
                    category);

            scrollViewCategory.addCategoriesToContent(scrollViewCategory.getViewModel()
                    .getChildCategories());

            content.addSubcategory(scrollViewCategory);
        }
    }

    private void addEntity(T entity) {
        if (viewModel.getShouldContain().test(entity)) {
            if (getViewModel().isEndCategory()) {
                viewModel.addEntity(entity);
                content.addEntity(entity,
                        viewModel.getViewType(),
                        viewModel.getPermissionLevel());
            }
            else {
                for (ScrollViewCategory<T> subcategory : content.subcategoryViews.values()) {
                    subcategory.addEntity(entity);
                }
            }
        }
    }

    private void hideIfEmpty() {
        if (viewModel.getChildCategories().size() == 0 &&
                viewModel.getChildEntities().size() == 0 &&
                !viewModel.isShowEmpty()) {
            setVisibility(View.GONE);
        }
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
