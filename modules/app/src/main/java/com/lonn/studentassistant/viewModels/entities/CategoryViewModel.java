package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.common.interfaces.Function;
import com.lonn.studentassistant.common.interfaces.Predicate;
import com.lonn.studentassistant.common.interfaces.Supplier;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.views.EntityViewType;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.views.EntityViewType.FULL;

public class CategoryViewModel<T extends BaseEntity> extends BaseObservable {
    private EntityViewType viewType = FULL;
    private boolean showHeader = true;
    private boolean showEmpty = true;
    private int permissionLevel = 0;
    private String categoryTitle = "Category Title";
    private String entityName = "entity";

    private List<CategoryViewModel<T>> childCategories = new ArrayList<>();
    private List<T> childEntities = new ArrayList<>();
    private Predicate<T> shouldContain = e -> true;

    public CategoryViewModel() {
    }

    public CategoryViewModel<T> addSubcategories(List<CategoryViewModel<T>> categoryViewModels) {
        childCategories.addAll(categoryViewModels);
        this.notifyPropertyChanged(BR.categoryModel);
        return this;
    }

    @Bindable
    public Predicate<T> getShouldContain() {
        return shouldContain;
    }

    public CategoryViewModel<T> setShouldContain(Predicate<T> shouldContain) {
        this.shouldContain = shouldContain;
        this.notifyPropertyChanged(BR.categoryModel);
        return this;
    }

    public CategoryViewModel<T> addEntity(T entity) {
        this.childEntities.add(entity);
        this.notifyPropertyChanged(BR.categoryModel);
        return this;
    }

    @Bindable
    public List<CategoryViewModel<T>> getChildCategories() {
        return childCategories;
    }

    public CategoryViewModel<T> setChildCategories(List<CategoryViewModel<T>> childCategories) {
        this.childCategories = childCategories;
        this.notifyPropertyChanged(BR.categoryModel);
        return this;
    }

    @Bindable
    public List<T> getChildEntities() {
        return childEntities;
    }

    public CategoryViewModel<T> setChildEntities(List<T> childEntities) {
        this.childEntities = childEntities;
        this.notifyPropertyChanged(BR.categoryModel);
        return this;
    }

    @Bindable
    public EntityViewType getViewType() {
        return viewType;
    }

    public CategoryViewModel<T> setViewType(EntityViewType viewType) {
        this.viewType = viewType;
        this.notifyPropertyChanged(BR.categoryModel);
        return this;
    }

    @Bindable
    public boolean isShowHeader() {
        return showHeader;
    }

    public CategoryViewModel<T> setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
        this.notifyPropertyChanged(BR.categoryModel);
        return this;
    }

    @Bindable
    public boolean isShowEmpty() {
        return showEmpty;
    }

    public CategoryViewModel<T> setShowEmpty(boolean showEmpty) {
        this.showEmpty = showEmpty;
        this.notifyPropertyChanged(BR.categoryModel);
        return this;
    }

    @Bindable
    public boolean isEndCategory() {
        return childCategories.size() == 0;
    }

    @Bindable
    public int getPermissionLevel() {
        return permissionLevel;
    }

    public CategoryViewModel<T> setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
        this.notifyPropertyChanged(BR.categoryModel);
        return this;
    }

    @Bindable
    public String getCategoryTitle() {
        return categoryTitle;
    }

    public CategoryViewModel<T> setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
        this.notifyPropertyChanged(BR.categoryModel);
        return this;
    }

    @Bindable
    public String getEntityName() {
        return entityName;
    }

    public CategoryViewModel<T> setEntityName(String entityName) {
        this.entityName = entityName;
        this.notifyPropertyChanged(BR.categoryModel);
        return this;
    }
}
