package com.lonn.studentassistant.views;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.lonn.studentassistant.common.interfaces.Comparator;
import com.lonn.studentassistant.common.interfaces.Function;
import com.lonn.studentassistant.common.interfaces.Predicate;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.viewModels.entities.CategoryViewModel;
import com.lonn.studentassistant.views.implementations.EntityView;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;

import java.util.List;

import static com.lonn.studentassistant.views.EntityViewType.valueOf;

public class BindingAdaptors {
    @BindingAdapter("srcCompat")
    public static void setSrcCompat(ImageView imageView, int resourceId) {
        imageView.setImageResource(resourceId);
    }

    @BindingAdapter("srcCompat")
    public static void setSrcCompat(ImageView imageView, String base64Resource) {
    }

    @BindingAdapter("android:childEntityComparator")
    public static <T extends BaseEntity> void setChildEntities(ScrollViewCategory<T> category,
                                                               Comparator<EntityView> comparator) {
        category.getViewModel().setEntitiesComparator(comparator);
        category.getContent().setEntityViewComparator(comparator);
    }


    @BindingAdapter("android:childEntities")
    public static <T extends BaseEntity> void setChildEntities(ScrollViewCategory<T> category,
                                                               List<T> entities) {
        if (entities != null && category != null) {
            category.addEntities(entities);
        }
    }

    @BindingAdapter("android:shouldContain")
    public static <T extends BaseEntity> void setShouldContain(ScrollViewCategory<T> category,
                                                               Predicate<T> shouldContain) {
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
    public static <T extends BaseEntity> void setSubcategoryGeneratorFunction(ScrollViewCategory<T> categoryView,
                                                                              Function<CategoryViewModel<T>, List<CategoryViewModel<T>>> subcategoryGeneratorFunction) {
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
