package com.lonn.studentassistant.views.implementations.category;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.RotateAnimation;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.animations.ExpandAnimation;
import com.lonn.studentassistant.databinding.CategoryLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.viewModels.CategoryViewModel;
import com.lonn.studentassistant.viewModels.entities.abstractions.EntityViewModel;
import com.lonn.studentassistant.views.abstractions.ScrollViewItem;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static com.lonn.studentassistant.R.id.arrowCategory;
import static com.lonn.studentassistant.R.id.layoutCategoryContent;
import static com.lonn.studentassistant.R.id.layoutCategoryHeader;
import static com.lonn.studentassistant.R.id.layoutCategoryMain;
import static com.lonn.studentassistant.R.layout.category_layout;

public class ScrollViewCategory<T extends EntityViewModel<? extends BaseEntity>> extends ScrollViewItem {
	protected boolean expanded = false, animated = false;
	private CategoryViewModel<T> viewModel = new CategoryViewModel<>();
	private ScrollViewCategoryHeader header;
	private ScrollViewCategoryContent<T> content;
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

	public CategoryLayoutBinding getBinding() {
		return binding;
	}

	public void setIsTable(Boolean isTable) {
		if (this.viewModel.isEndCategory()) {
			if (isTable == null) {
				isTable = false;
			}
			binding.setIsTable(isTable);
		}
		else {
			binding.setIsTable(false);
		}

		for (ScrollViewCategory category : content.subcategoryViews.values()) {
			category.setIsTable(isTable);
		}
	}

	public CategoryViewModel<T> getViewModel() {
		return viewModel;
	}

	public void addChildCategories(Collection<CategoryViewModel<T>> categories) {
		getViewModel().addSubcategories(categories);

		addCategoriesToContent(categories);
		setIsTable(binding.getIsTable());

		hideIfEmpty();
	}

	public void setEntities(Collection<T> entities) {
		for (T entity : entities) {
			addOrUpdateEntity(entity);
		}

		List<String> entitiesToRemove = new LinkedList<>();

		for (String existingEntityKey : viewModel.getChildEntities().keySet()) {
			boolean found = false;

			for (T receivedEntity : entities) {
				if (receivedEntity.getKey().equals(existingEntityKey)) {
					found = true;
					break;
				}
			}

			if (!found) {
				entitiesToRemove.add(existingEntityKey);
			}
		}

		for (String entityToRemove : entitiesToRemove) {
			viewModel.getChildEntities().remove(entityToRemove);
			content.removeEntityByKey(entityToRemove);
		}

		for (ScrollViewCategory<T> subcategory : content.subcategoryViews.values()) {
			subcategory.setEntities(entities);
		}

		hideIfEmpty();
	}

	public ScrollViewCategoryContent<T> getContent() {
		return content;
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

			content.addSubcategory(scrollViewCategory);
		}
	}

	private void addOrUpdateEntity(T entity) {
		if (viewModel.getShouldContain().test(entity)) {
			if (getViewModel().isEndCategory()) {
				viewModel.addEntity(entity);
				content.addOrUpdateEntity(entity,
						viewModel.getViewType(),
						viewModel.getPermissionLevel());
			}
		}
	}

	private void hideIfEmpty() {
		if (getNumberOfChildren() == 0 &&
				!viewModel.isShowEmpty()) {
			setVisibility(GONE);
		}
		else if (getNumberOfChildren() != 0) {
			setVisibility(VISIBLE);
		}
	}

	private void animateExpand() {
		if (!animated) {
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

	public void setOnAddAction(Runnable runnable) {
		this.getContent().setOnAddTap(runnable);
	}

	public void setOnDeleteAction(Consumer<T> onDeleteTap) {
		this.getContent().setOnDeleteTap(onDeleteTap);
	}
}
