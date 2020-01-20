package com.lonn.studentassistant.views.implementations.category;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.functionalIntefaces.Comparator;
import com.lonn.studentassistant.views.EntityViewType;
import com.lonn.studentassistant.views.implementations.EntityView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.lonn.studentassistant.views.implementations.EntityViewComparatorHolder.ASCENDING_TITLE_COMPARATOR;

public class ScrollViewCategoryContent<T extends EntityViewModel<? extends BaseEntity>> extends LinearLayout {
	protected Map<String, EntityView<T>> childEntityViews = new HashMap<>();
	protected Map<String, ScrollViewCategory<T>> subcategoryViews = new HashMap<>();
	private Comparator<EntityView> entityViewComparator = ASCENDING_TITLE_COMPARATOR;
	private Consumer<T> onDelete;

	public ScrollViewCategoryContent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void removeAllSubcategories() {
		for (ScrollViewCategory category : subcategoryViews.values()) {
			removeView(category);
		}

		subcategoryViews.clear();
	}

	public void setOnAddTap(Runnable runnable) {
		findViewById(R.id.layoutCategoryAdd).setOnClickListener(v -> runnable.run());
	}

	public void addOrUpdateEntity(T entity, EntityViewType viewType, int permissionLevel) {
		EntityView<T> entityView = childEntityViews.get(entity.getKey());

		if (entityView == null) {
			entityView = new EntityView<>(getContext(),
					viewType,
					permissionLevel,
					entity);

			addView(entityView);

			if (onDelete != null) {
				entityView.setOnDeleteTap(onDelete);
			}
		}
		else {
			entityView.updateEntity(entity);
		}

		childEntityViews.put(entity.getKey(),
				entityView);
	}

	public void addSubcategory(ScrollViewCategory<T> subCategory) {
		subcategoryViews.put(subCategory.getViewModel().getCategoryTitle(),
				subCategory);

		addView(subCategory);
	}

	public void setEntityViewComparator(Comparator<EntityView> entityViewComparator) {
		this.entityViewComparator = entityViewComparator;
	}

	public void removeEntityByKey(String entityKey) {
		EntityView<T> entityView = childEntityViews.get(entityKey);

		if (entityView != null) {
			removeView(entityView);
			childEntityViews.remove(entityKey);
		}
	}

	public Collection<ScrollViewCategory<T>> getSubcategories() {
		return subcategoryViews.values();
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		if (getChildCount() == 0) {
			index = 0;
		}
		else {
			if (child instanceof EntityView) {
				index = getIndexToSort((EntityView) child, entityViewComparator);
			}
			else {
				index = getChildCount() - 1;
			}
		}

		super.addView(child, index, params);
	}

	private int getIndexToSort(EntityView entityToBeAdded, Comparator<EntityView> comparator) {
		for (int i = 0; i < this.getChildCount(); i++) {
			View child = this.getChildAt(i);

			if (child instanceof EntityView &&
					comparator.compare((EntityView) child, entityToBeAdded) >= 0) {
				return i;
			}
		}

		return this.getChildCount() - 1;
	}

	public void setOnDeleteTap(Consumer<T> onDelete) {
		if (onDelete != null) {
			this.onDelete = onDelete;

			for (EntityView<T> entityView : childEntityViews.values()) {
				entityView.setOnDeleteTap(onDelete);
			}
		}
	}
}
