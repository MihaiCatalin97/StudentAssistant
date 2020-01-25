package com.lonn.studentassistant.viewModels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.functionalIntefaces.Comparator;
import com.lonn.studentassistant.functionalIntefaces.Function;
import com.lonn.studentassistant.functionalIntefaces.Predicate;
import com.lonn.studentassistant.views.EntityViewType;
import com.lonn.studentassistant.views.implementations.EntityView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lonn.studentassistant.views.EntityViewType.FULL;

public class CategoryViewModel<T extends EntityViewModel<? extends BaseEntity>> extends BaseObservable {
	private EntityViewType viewType = FULL;
	private boolean showHeader = true;
	private boolean showEmpty = true;
	private int permissionLevel = 0;
	private String categoryTitle = "Category Title";
	private String entityName = "entityViewModel";
	private Comparator<EntityView> entitiesComparator;
	private Runnable onAdd;
	private Function<CategoryViewModel<T>, List<CategoryViewModel<T>>> generatorFunction;

	private Collection<CategoryViewModel<T>> childCategories = new ArrayList<>();
	private Map<String, T> childEntities = new HashMap<>();
	private Predicate<T> shouldContain = e -> true;

	public CategoryViewModel() {
	}

	public Function<CategoryViewModel<T>, List<CategoryViewModel<T>>> getGeneratorFunction() {
		return generatorFunction;
	}

	public Runnable getOnAdd() {
		return onAdd;
	}

	public CategoryViewModel<T> setOnAdd(Runnable onAdd) {
		this.onAdd = onAdd;
		return this;
	}

	public void setGeneratorFunction(Function<CategoryViewModel<T>, List<CategoryViewModel<T>>> generatorFunction) {
		this.generatorFunction = generatorFunction;
	}

	public CategoryViewModel<T> addSubcategories(Collection<CategoryViewModel<T>> categoryViewModels) {
		childCategories.addAll(categoryViewModels);
		this.notifyPropertyChanged(BR.categoryModel);
		return this;
	}

	@Bindable
	public Comparator<EntityView> getEntitiesComparator() {
		return entitiesComparator;
	}

	public CategoryViewModel<T> setEntitiesComparator(Comparator<EntityView> entitiesComparator) {
		this.entitiesComparator = entitiesComparator;
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
		this.childEntities.put(entity.getKey(), entity);
		this.notifyPropertyChanged(BR.categoryModel);
		return this;
	}

	@Bindable
	public Collection<CategoryViewModel<T>> getChildCategories() {
		return childCategories;
	}

	public CategoryViewModel<T> setChildCategories(Collection<CategoryViewModel<T>> childCategories) {
		this.childCategories = childCategories;
		this.notifyPropertyChanged(BR.categoryModel);
		return this;
	}

	@Bindable
	public Map<String, T> getChildEntities() {
		return childEntities;
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
