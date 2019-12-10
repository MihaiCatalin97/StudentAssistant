package com.lonn.studentassistant.views.implementations.categories.otherActivityCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.views.abstractions.category.ScrollViewCategory;

public class OtherActivityBaseCategory extends ScrollViewCategory<OtherActivity> {

	public OtherActivityBaseCategory(Context context) {
		super(context);
	}

	public OtherActivityBaseCategory(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OtherActivityBaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public boolean shouldContain(OtherActivity otherActivity) {
		return true;
	}

	public void generateChildCategories(OtherActivity otherActivity) {
		if (getViewModel().getSubCategoriesToGenerate().equals("types")) {
			if (!containsChildCategoryWithName(otherActivity.getType())) {
				OtherActivityTypeCategory newCategory = new OtherActivityTypeCategory(getContext());

				newCategory.getViewModel().setCategoryTitle(otherActivity.getType());
				addChildCategory(newCategory);
			}

			getContent().sortChildren();
		}
	}

	protected void initCategoryViewModel() {
		viewModel.setEntityName("other activity");

		viewModel.notifyPropertyChanged(com.lonn.studentassistant.BR.entityName);
	}
}
