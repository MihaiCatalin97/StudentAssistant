package com.lonn.studentassistant.views;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.lonn.studentassistant.common.interfaces.Predicate;
import com.lonn.studentassistant.views.implementations.categories.courseCategories.CourseSemesterCategory;

import java.util.List;

public class BindingAdaptors {
	@BindingAdapter("srcCompat")
	public static void bindSrcCompat(ImageView imageView, int resourceId) {
		imageView.setImageResource(resourceId);
	}

	@BindingAdapter("childEntities")
	public static void createChildEntities(CourseSemesterCategory category,
										   List entities) {
		if (entities != null && category != null) {
			category.addEntities(entities);
		}
	}

	@BindingAdapter("should_contain")
	public static void setShouldContain(CourseSemesterCategory category,
										Predicate shouldContain) {
		category.getViewModel().setShouldContain(shouldContain);
	}

	@BindingAdapter("view_type")
	public static void setViewType(CourseSemesterCategory category,
								   String type) {
		category.getViewModel().setViewType(EntityViewType.valueOf(type.toUpperCase()));
	}

	@BindingAdapter("show_empty")
	public static void setShowEmpty(CourseSemesterCategory category,
									Boolean showEmpty) {
		category.getViewModel().setShowEmpty(showEmpty);
	}

	@BindingAdapter("show_header")
	public static void setShowHeader(CourseSemesterCategory category,
									 Boolean showHeader) {
		category.getViewModel().setShowHeader(showHeader);
	}

	@BindingAdapter("category_title")
	public static void setCategoryTitle(CourseSemesterCategory category,
										String title) {
		category.getViewModel().setCategoryTitle(title);
	}

	@BindingAdapter("subcategories_to_generate")
	public static void setChildCategoryTypes(CourseSemesterCategory category,
											 String subCategoriesToGenerate) {
		category.getViewModel().setSubCategoriesToGenerate(subCategoriesToGenerate);
	}

	@BindingAdapter("permission_level")
	public static void setPermissionLevel(CourseSemesterCategory category,
										  Integer permissionLevel) {
		category.getViewModel().setPermissionLevel(permissionLevel);
	}

	@BindingAdapter("entity_name")
	public static void setEntityName(CourseSemesterCategory category,
									 String entityName) {
		category.getViewModel().setEntityName(entityName);
	}
}
