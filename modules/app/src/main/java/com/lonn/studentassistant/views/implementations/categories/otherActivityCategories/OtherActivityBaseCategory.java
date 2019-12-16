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
}
