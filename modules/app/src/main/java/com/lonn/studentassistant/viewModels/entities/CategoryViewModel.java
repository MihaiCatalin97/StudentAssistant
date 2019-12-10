package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.common.interfaces.Predicate;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.views.EntityViewType;

import java.util.ArrayList;
import java.util.List;

import static com.lonn.studentassistant.views.EntityViewType.FULL;

public class CategoryViewModel<T extends BaseEntity> extends BaseObservable {
	private EntityViewType viewType = FULL;
	private boolean showHeader = true;
	private boolean showEmpty = true;
	private String subCategoriesToGenerate = "none";
	private int permissionLevel = 0;
	private String categoryTitle = "Category Title";
	private String entityName = "entity";

	private List<CategoryViewModel<T>> childCategories = new ArrayList<>();
	private List<T> childEntities = new ArrayList<>();
	private Predicate<T> shouldContain = e -> true;

	public CategoryViewModel() {
	}

	public String getSubCategoriesToGenerate() {
		return subCategoriesToGenerate;
	}

	public void setSubCategoriesToGenerate(String subCategoriesToGenerate) {
		this.subCategoriesToGenerate = subCategoriesToGenerate;
		this.notifyPropertyChanged(BR.categoryModel);
	}

	public void addSubcategory(CategoryViewModel<T> categoryViewModel) {
		childCategories.add(categoryViewModel);
		this.notifyPropertyChanged(BR.categoryModel);
	}

	@Bindable
	public Predicate<T> getShouldContain() {
		return shouldContain;
	}

	public void setShouldContain(Predicate<T> shouldContain) {
		this.shouldContain = shouldContain;
		this.notifyPropertyChanged(BR.categoryModel);
	}

	public void addEntity(T entity) {
		this.childEntities.add(entity);
		this.notifyPropertyChanged(BR.categoryModel);
	}

	@Bindable
	public List<CategoryViewModel<T>> getChildCategories() {
		return childCategories;
	}

	public void setChildCategories(List<CategoryViewModel<T>> childCategories) {
		this.childCategories = childCategories;
		this.notifyPropertyChanged(BR.categoryModel);
	}

	@Bindable
	public List<T> getChildEntities() {
		return childEntities;
	}

	public void setChildEntities(List<T> childEntities) {
		this.childEntities = childEntities;
		this.notifyPropertyChanged(BR.categoryModel);
	}

	@Bindable
	public EntityViewType getViewType() {
		return viewType;
	}

	public void setViewType(EntityViewType viewType) {
		this.viewType = viewType;
		this.notifyPropertyChanged(BR.categoryModel);
	}

	@Bindable
	public boolean isShowHeader() {
		return showHeader;
	}

	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
		this.notifyPropertyChanged(BR.categoryModel);
	}

	@Bindable
	public boolean isShowEmpty() {
		return showEmpty;
	}

	public void setShowEmpty(boolean showEmpty) {
		this.showEmpty = showEmpty;
		this.notifyPropertyChanged(BR.categoryModel);
	}

	@Bindable
	public boolean isEndCategory() {
		return childCategories.size() == 0;
	}

	@Bindable
	public int getPermissionLevel() {
		return permissionLevel;
	}

	public void setPermissionLevel(int permissionLevel) {
		this.permissionLevel = permissionLevel;
		this.notifyPropertyChanged(BR.categoryModel);
	}

	@Bindable
	public String getCategoryTitle() {
		return categoryTitle;
	}

	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
		this.notifyPropertyChanged(BR.categoryModel);
	}

	@Bindable
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
		this.notifyPropertyChanged(BR.categoryModel);
	}
}
