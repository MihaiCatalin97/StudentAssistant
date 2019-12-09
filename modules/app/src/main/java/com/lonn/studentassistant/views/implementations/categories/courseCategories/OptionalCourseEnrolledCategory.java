package com.lonn.studentassistant.views.implementations.categories.courseCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.entities.Course;

public class OptionalCourseEnrolledCategory extends OptionalCourseBaseCategory {
	public OptionalCourseEnrolledCategory(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OptionalCourseEnrolledCategory(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public boolean shouldContain(Course course) {
		return true;
	}

	public void generateChildCategories(Course course) {
	}
}
