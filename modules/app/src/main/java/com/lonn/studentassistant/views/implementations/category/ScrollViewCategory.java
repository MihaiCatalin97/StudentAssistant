package com.lonn.studentassistant.views.implementations.category;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.animations.ExpandAnimation;
import com.lonn.studentassistant.databinding.CategoryLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.viewModels.CategoryViewModel;
import com.lonn.studentassistant.views.abstractions.ScrollViewItem;
import com.lonn.studentassistant.views.implementations.EntityView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static com.lonn.studentassistant.R.id.arrowCategory;
import static com.lonn.studentassistant.R.id.layoutCategoryContent;
import static com.lonn.studentassistant.R.id.layoutCategoryHeader;
import static com.lonn.studentassistant.R.id.layoutCategoryMain;
import static com.lonn.studentassistant.R.layout.category_layout;

public class ScrollViewCategory<T extends EntityViewModel<? extends BaseEntity>> extends ScrollViewItem {
	protected boolean expanded = false, animated = false;
	private ScrollViewCategoryHeader header;
	@Getter
	private CategoryViewModel<T> viewModel = new CategoryViewModel<>();
	@Getter
	private ScrollViewCategoryContent<T> content;
	@Getter
	private CategoryLayoutBinding binding;

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

	public void setIsTable(Boolean isTable) {
		if (isTable == null) {
			isTable = false;
		}
		binding.setIsTable(isTable);

		for (ScrollViewCategory category : content.subcategoryViews.values()) {
			category.setIsTable(isTable);
		}
	}

	public void setEditing(Boolean editing) {
		binding.setEditing(editing);
		hideIfEmpty();

		for (ScrollViewCategory<T> subcategory : content.subcategoryViews.values()) {
			subcategory.setEditing(editing);
		}

		for (EntityView<T> childEntityView : content.childEntityViews.values()) {
			childEntityView.setEditing(editing);
		}
	}

	public void setShowAddButtonFirstLayer(Boolean showAddButtonFirstLayer) {
		if (showAddButtonFirstLayer == null) {
			showAddButtonFirstLayer = false;
		}

		for (ScrollViewCategory subcategory : content.getSubcategories()) {
			subcategory.setShowAddButtonFirstLayer(showAddButtonFirstLayer);
		}

		binding.setShowAddButtonFirstLayer(showAddButtonFirstLayer);
	}

	public void setUnlinkable(Boolean unlinkable) {
		for (ScrollViewCategory<T> subcategory : content.subcategoryViews.values()) {
			subcategory.setUnlinkable(unlinkable);
		}

		content.setUnlinkable(unlinkable);
	}

	public void setDeletable(Boolean deletable) {
		for (ScrollViewCategory<T> subcategory : content.subcategoryViews.values()) {
			subcategory.setDeletable(deletable);
		}

		content.setDeletable(deletable);
	}

	public void setChildCategories(Collection<CategoryViewModel<T>> categories) {
		getViewModel().setChildCategories(categories);
		List<ScrollViewCategory<T>> categoriesToBeRemoved = new LinkedList<>();
		Collection<CategoryViewModel<T>> categoriesToBeAdded = new LinkedList<>();

		for (ScrollViewCategory<T> category : content.getSubcategories()) {
			boolean found = false;

			for (CategoryViewModel<T> categoryViewModel : categories) {
				if (category.getViewModel().getCategoryTitle().equals(categoryViewModel.getCategoryTitle())) {
					found = true;
					category.getViewModel().setShouldContain(categoryViewModel.getShouldContain());
					break;
				}
			}

			if (!found) {
				categoriesToBeRemoved.add(category);
			}
		}

		for (CategoryViewModel<T> categoryViewModel : categories) {
			boolean found = false;

			for (ScrollViewCategory category : content.getSubcategories()) {
				if (category.getViewModel().getCategoryTitle().equals(categoryViewModel.getCategoryTitle())) {
					found = true;
					break;
				}
			}

			if (!found) {
				categoriesToBeAdded.add(categoryViewModel);
			}
		}

		for (ScrollViewCategory<T> category : categoriesToBeRemoved) {
			content.removeSubcategory(category);

			for (String removedEntityKey : category.getViewModel().getChildEntities().keySet()) {
				viewModel.getChildEntities().remove(removedEntityKey);
			}
		}
		setIsTable(binding.getIsTable());
		addCategoriesToContent(categoriesToBeAdded);

		hideIfEmpty();
	}

	public void setEntities(Collection<T> entities) {
		if (entities != null) {
			for (T entity : entities) {
				addOrUpdateEntity(entity);
			}
		}

		removeNonExistingEntities(entities);

		for (ScrollViewCategory<T> subcategory : content.subcategoryViews.values()) {
			subcategory.removeNonExistingEntities(entities);
		}
	}

	protected void initContent() {
		content = findViewById(layoutCategoryMain).findViewById(layoutCategoryContent);
		header = findViewById(layoutCategoryMain).findViewById(layoutCategoryHeader);

		header.setOnClickListener(v -> animateExpand());

		header.bringToFront();
	}

	@Override
	protected void inflateLayout(Context context) {
		LayoutInflater layoutInflater = (LayoutInflater)
				context.getSystemService(LAYOUT_INFLATER_SERVICE);
		if (layoutInflater != null) {
			binding = DataBindingUtil.inflate(
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
		this.getContent().setOnAddTap(viewModel.getOnAdd());
	}

	protected int getNumberOfChildren() {
		int result = getViewModel().getChildEntities().values().size();

		for (ScrollViewCategory<T> category : getContent().subcategoryViews.values()) {
			result += category.getNumberOfChildren();
		}

		return result;
	}

	private void addCategoriesToContent(Collection<CategoryViewModel<T>> categories) {
		for (CategoryViewModel<T> category : categories) {
			ScrollViewCategory<T> scrollViewCategory = new ScrollViewCategory<>(getContext(),
					category);

			scrollViewCategory.addCategoriesToContent(scrollViewCategory.getViewModel()
					.getChildCategories());

			scrollViewCategory.setEditing(binding.getEditing());
			scrollViewCategory.setShowAddButtonFirstLayer(binding.getShowAddButtonFirstLayer());
			scrollViewCategory.setIsTable(binding.getIsTable());

			content.addSubcategory(scrollViewCategory);
		}
	}

	private void addOrUpdateEntity(T entity) {
		if (viewModel.getShouldContain().test(entity)) {
			if (getViewModel().isEndCategory()) {
				viewModel.addEntity(entity);
				content.addOrUpdateEntity(entity,
						viewModel.getViewType(),
						viewModel.getPermissionLevel(),
						binding.getEditing());
			}
			else {
				for (ScrollViewCategory<T> subcategory : content.subcategoryViews.values()) {
					subcategory.addOrUpdateEntity(entity);
				}
			}
		}
	}

	private void removeNonExistingEntities(Collection<T> entities) {
		List<String> entitiesToRemove = new LinkedList<>(viewModel.getChildEntities().keySet());

		if (entities != null) {
			for (T receivedEntity : entities) {
				entitiesToRemove.remove(receivedEntity.getKey());
			}
		}

		for (String entityToRemove : entitiesToRemove) {
			viewModel.getChildEntities().remove(entityToRemove);
			content.removeEntityByKey(entityToRemove);
		}

		hideIfEmpty();
	}

	private void hideIfEmpty() {
		if (getBinding().getEditing() != null && getBinding().getEditing()) {
			setVisibility(VISIBLE);
		}
		else if (getNumberOfChildren() == 0 &&
				!viewModel.isShowEmpty() && binding.getEditing() != null && !binding.getEditing()) {
			setVisibility(GONE);
		}
		else if (getNumberOfChildren() != 0) {
			setVisibility(VISIBLE);
		}
	}

	private void animateExpand() {
		if (!animated) {
			expanded = !expanded;
			animated = true;

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

			getHandler().postDelayed(() -> animated = false, 500);
		}
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		if (child.getId() != R.id.tableHeader) {
			super.addView(child, index, params);
		}
		else {
			content.addView(child, index, params);
		}
	}

	public void setOnAddAction(Runnable runnable) {
		for (ScrollViewCategory subcategory : getContent().getSubcategories()) {
			subcategory.setOnAddAction(runnable);
		}

		this.viewModel.setOnAdd(runnable);
		this.getContent().setOnAddTap(runnable);
	}

	public void setOnRemoveAction(Consumer<T> onRemoveTap) {
		for (ScrollViewCategory<T> subcategory : content.getSubcategories()) {
			subcategory.setOnRemoveAction(onRemoveTap);
		}

		this.getContent().setOnRemoveTap(onRemoveTap);
	}

	public void setOnDeleteAction(Consumer<T> onDeleteTap) {
		for (ScrollViewCategory<T> subcategory : content.getSubcategories()) {
			subcategory.setOnDeleteAction(onDeleteTap);
		}

		this.getContent().setOnDeleteTap(onDeleteTap);
	}
}
