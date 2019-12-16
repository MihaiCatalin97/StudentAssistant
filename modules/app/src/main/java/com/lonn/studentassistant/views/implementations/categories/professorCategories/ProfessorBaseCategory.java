package com.lonn.studentassistant.views.implementations.categories.professorCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.views.abstractions.category.ScrollViewCategory;

public class ProfessorBaseCategory extends ScrollViewCategory<Professor> {

	public ProfessorBaseCategory(Context context) {
		super(context);
	}

	public ProfessorBaseCategory(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
}
