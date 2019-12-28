package com.lonn.studentassistant.databinding;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.functionalIntefaces.Comparator;
import com.lonn.studentassistant.functionalIntefaces.Function;
import com.lonn.studentassistant.functionalIntefaces.Predicate;
import com.lonn.studentassistant.viewModels.CategoryViewModel;
import com.lonn.studentassistant.viewModels.entities.EntityViewModel;
import com.lonn.studentassistant.views.implementations.EntityView;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;

import java.util.List;

import static com.lonn.studentassistant.views.EntityViewType.valueOf;

public class BindingAdapters {
    @BindingAdapter("android:layout_marginBottom")
    public static void setLayoutMarginBottom(View view, float margin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.bottomMargin = (int) margin;
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("android:layout_marginLeft")
    public static void setLayoutMarginLeft(View view, float margin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.leftMargin = (int) margin;
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("android:layout_marginTop")
    public static void setLayoutMarginTop(View view, float margin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.topMargin = (int) margin;
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("android:layout_marginRight")
    public static void setLayoutMarginRight(View view, float margin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.rightMargin = (int) margin;
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("srcCompat")
    public static void setSrcCompat(ImageView imageView, int resourceId) {
        imageView.setImageResource(resourceId);
    }

    @BindingAdapter("srcCompat")
    public static void setSrcCompat(ImageView imageView, String base64Resource) {
    }

    @BindingAdapter("android:childEntityComparator")
    public static <T extends BaseEntity, U extends EntityViewModel<T>> void setChildEntityComparator(ScrollViewCategory<T, U> category,
                                                                                                     Comparator<EntityView> comparator) {
        category.getViewModel().setEntitiesComparator(comparator);
        category.getContent().setEntityViewComparator(comparator);
    }


    @BindingAdapter("android:childEntities")
    public static <T extends BaseEntity, U extends EntityViewModel<T>> void setChildEntities(ScrollViewCategory<T, U> category,
                                                                                             List<U> entities) {
        if (entities != null && category != null) {
            category.addEntities(entities);
        }
    }

    @BindingAdapter("android:shouldContain")
    public static <T extends BaseEntity, U extends EntityViewModel<T>> void setShouldContain(ScrollViewCategory<T, U> category,
                                                                                             Predicate<U> shouldContain) {
        category.getViewModel()
                .setShouldContain(shouldContain);
    }

    @BindingAdapter("android:viewType")
    public static void setViewType(ScrollViewCategory category,
                                   String type) {
        category.getViewModel()
                .setViewType(valueOf(type.toUpperCase()));
    }

    @BindingAdapter("android:showEmpty")
    public static void setShowEmpty(ScrollViewCategory category,
                                    Boolean showEmpty) {
        category.getViewModel()
                .setShowEmpty(showEmpty);
    }

    @BindingAdapter("android:showHeader")
    public static void bindShowHeader(ScrollViewCategory category,
                                      Boolean showHeader) {
        category.getViewModel()
                .setShowHeader(showHeader);
    }

    @BindingAdapter("android:subcategoryGeneratorFunction")
    public static <T extends BaseEntity, U extends EntityViewModel<T>> void setSubcategoryGeneratorFunction(ScrollViewCategory<T, U> categoryView,
                                                                                                            Function<CategoryViewModel<T, U>, List<CategoryViewModel<T, U>>> subcategoryGeneratorFunction) {
        categoryView.addChildCategories(subcategoryGeneratorFunction
                .apply(categoryView.getViewModel()));
    }

    @BindingAdapter("android:permissionLevel")
    public static void setPermissionLevel(ScrollViewCategory category,
                                          Integer permissionLevel) {
        category.getViewModel()
                .setPermissionLevel(permissionLevel);
    }

    @BindingAdapter("android:entityName")
    public static void setEntityName(ScrollViewCategory category,
                                     String entityName) {
        category.getViewModel()
                .setEntityName(entityName);
    }

    @BindingAdapter("android:categoryTitle")
    public static void setCategoryTitle(ScrollViewCategory category, String title) {
        if (category != null && category.getViewModel() != null) {
            category.getViewModel()
                    .setCategoryTitle(title);
        }
    }
}
