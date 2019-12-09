package com.lonn.studentassistant.views.abstractions.category;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.viewModels.entities.CategoryViewModel;
import com.lonn.studentassistant.views.abstractions.ScrollViewItem;
import com.lonn.studentassistant.views.implementations.EntityView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScrollViewCategoryContent<T extends BaseEntity> extends LinearLayout {
	private boolean showEmpty;
	private CategoryViewModel categoryViewModel;
	protected String viewType = "full";
	protected Map<String, EntityView<T>> childEntityViews = new HashMap<>();
	protected Map<String, ScrollViewCategory<T>> childCategories = new HashMap<>();
	protected ConstraintLayout categoryAddLayout;

	public ScrollViewCategoryContent(Context context) {
		super(context);
	}

	public ScrollViewCategoryContent(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttributesFromSet(attrs);
		initContent();
	}

	public ScrollViewCategoryContent(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getAttributesFromSet(attrs);
		initContent();
	}

	public void setCategoryViewModel(CategoryViewModel categoryViewModel) {
		this.categoryViewModel = categoryViewModel;
	}


	protected void initContent() {
		if (getChildCount() == 1 && !showEmpty) {
			setVisibility(View.GONE);
		}
	}

	private void getAttributesFromSet(AttributeSet set) {
		TypedArray a = getContext().obtainStyledAttributes(set, R.styleable.ScrollViewCategoryContent);
		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i) {
			int attr = a.getIndex(i);
			switch (attr) {
				case R.styleable.ScrollViewCategoryContent_show_empty:
					showEmpty = a.getBoolean(attr, false);
					break;
				case R.styleable.ScrollViewCategoryContent_view_type:
					viewType = a.getString(attr);
					break;
			}
		}
		a.recycle();
	}

	public void addChildCategory(ScrollViewCategory<T> category) {
		ScrollViewCategory<T> existingCategory =
				childCategories.get(category.getCategoryViewModel().categoryName);


		if (existingCategory != null)
			return;

		childCategories.put(category.getCategoryViewModel().categoryName,
				category);
		addView(category);
	}

	public void addEntityView(T entity) {
		if (categoryViewModel.isEndCategory) {
			EntityView<T> view = childEntityViews.get(entity.getKey());

			if (view != null) {
				view.addOrUpdate(entity);
			}
			else {
				view = new EntityView<>(getContext(), entity, viewType, categoryViewModel.permissionLevel);
				childEntityViews.put(entity.getKey(), view);
				addView(view);
			}
		}
		else {
			for (ScrollViewItem<T> child : childEntityViews.values()) {
				child.addOrUpdate(entity);
			}
		}
	}

	protected void sortChildren() {
		String[] categories = new String[childEntityViews.size()];
		categories = childEntityViews.keySet().toArray(categories);
		Arrays.sort(categories);

		for (String childCategory : categories) {
			View child = childEntityViews.get(childCategory);

			if (child != null) {
				child.bringToFront();
			}
		}

		categoryAddLayout.bringToFront();
	}

	private void setChildCategoriesViewType() {
		for (ScrollViewItem child : childEntityViews.values()) {
			if (child instanceof ScrollViewCategory) {
				((ScrollViewCategory) child).getCategoryViewModel().viewType = viewType;
			}
		}
	}

	public void delete(String key) {
		if (categoryViewModel.isEndCategory) {
			ScrollViewItem<T> view = childEntityViews.get(key);

			if (view != null) {
				childEntityViews.remove(key);
				removeView(view);
			}
		}
		else {
			deleteCascade(key);
		}
	}

	private void deleteCascade(String key) {
		for (ScrollViewItem<T> category : childEntityViews.values()) {
			if (category instanceof ScrollViewCategory) {
				((ScrollViewCategory<T>) category).getCategoryContentLayout().delete(key);
			}
		}
	}

	public void setOnAddTap(Runnable runnable) {
		categoryAddLayout.setOnClickListener(v -> runnable.run());
	}

	public List<String> getChildCategoryNames() {
		return new ArrayList<>(childCategories.keySet());
	}

	public boolean containsChildCategoryWithName(String categoryName) {
		return childCategories.keySet().contains(categoryName);
	}
}
