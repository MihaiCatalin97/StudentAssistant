package com.lonn.studentassistant.views.implementations.categories.scheduleCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.views.abstractions.category.ScrollViewCategory;

public class ScheduleBaseCategory extends ScrollViewCategory<RecurringClass> {
	public ScheduleBaseCategory(Context context) {
		super(context);
	}

	public ScheduleBaseCategory(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScheduleBaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public boolean shouldContain(RecurringClass scheduleClass) {
		return true;
	}

	protected void initCategoryViewModel() {
		viewModel.setEntityName("schedule class");

		viewModel.notifyPropertyChanged(com.lonn.studentassistant.BR.entityName);
	}
}