package com.lonn.studentassistant.databinding;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.functionalIntefaces.Comparator;
import com.lonn.studentassistant.functionalIntefaces.Function;
import com.lonn.studentassistant.functionalIntefaces.Predicate;
import com.lonn.studentassistant.viewModels.CategoryViewModel;
import com.lonn.studentassistant.views.implementations.EntityView;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;

import java.util.Collection;
import java.util.List;

import static com.lonn.studentassistant.views.EntityViewType.valueOf;

public class BindingAdapters {
	@BindingAdapter(value = {"android:layout_marginEnd", "android:layout_marginStart",
			"android:layout_marginLeft", "android:layout_marginRight",
			"android:layout_marginTop",
			"android:layout_marginBottom"}, requireAll = false)
	public static void setMargins(View view, float marginEnd, float marginStart,
								  float marginLeft, float marginRight,
								  float marginTop,
								  float marginBottom) {
		int leftMargin = marginLeft == 0 ? (int) marginStart : (int) marginLeft;
		int rightMargin = marginRight == 0 ? (int) marginEnd : (int) marginRight;

		if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
			layoutParams.rightMargin = rightMargin;
			layoutParams.leftMargin = leftMargin;
			layoutParams.topMargin = (int) marginTop;
			layoutParams.bottomMargin = (int) marginBottom;
			view.setLayoutParams(layoutParams);
		}
	}

	@BindingAdapter("srcCompat")
	public static void setSrcCompat(ImageView imageView, int resourceId) {
		imageView.setImageResource(resourceId);
	}

	@BindingAdapter("defaultSrcCompat")
	public static void setDefaultSrcCompat(ImageView imageView, int resourceId) {
		if (imageView.getDrawable() == null) {
			imageView.setImageResource(resourceId);
		}
	}

	@BindingAdapter("roundedSrc")
	public static void setRoundedSrc(ImageView imageView, int resourceId) {
		imageView.setImageResource(resourceId);
	}

	@BindingAdapter("defaultRoundedSrc")
	public static void setDefaultRoundedSrc(ImageView imageView, int resourceId) {
		if (imageView.getDrawable() == null) {
			imageView.setImageResource(resourceId);
		}
	}

	@BindingAdapter("srcCompat")
	public static void setSrcCompat(ImageView imageView, String base64Resource) {
		if (imageView.getId() == R.id.professorImage) {
			imageView.setImageResource(R.drawable.default_person_image_male);
		}
	}

	@BindingAdapter("android:childEntityComparator")
	public static <T extends EntityViewModel<? extends BaseEntity>> void setChildEntityComparator(ScrollViewCategory<T> category,
																								  Comparator<EntityView> comparator) {
		category.getViewModel().setEntitiesComparator(comparator);
		category.getContent().setEntityViewComparator(comparator);
	}

	@BindingAdapter("android:childEntities")
	public static <T extends EntityViewModel<? extends BaseEntity>> void setChildEntities(ScrollViewCategory<T> category,
																						  Collection<T> entities) {
		if (entities != null && category != null) {
			if (category.getViewModel().getGeneratorFunction() != null) {
				category.addChildCategories(category.getViewModel().getGeneratorFunction()
						.apply(category.getViewModel()));
			}
			category.setEntities(entities);
		}
	}

	@BindingAdapter("android:isTable")
	public static void setIsTable(ScrollViewCategory category,
								  Boolean isTable) {
		if (isTable != null && isTable) {
			category.setIsTable(isTable);
		}
	}

	@BindingAdapter("android:shouldContain")
	public static <T extends EntityViewModel<? extends BaseEntity>> void setShouldContain(ScrollViewCategory<T> category,
																						  Predicate<T> shouldContain) {
		category.getViewModel()
				.setShouldContain(shouldContain);
	}

	@BindingAdapter("android:viewType")
	public static void setViewType(ScrollViewCategory category,
								   String type) {
		category.getViewModel()
				.setViewType(valueOf(type.toUpperCase()));
	}

	@BindingAdapter("android:showEmpty")
	public static void setShowEmpty(ScrollViewCategory category,
									Boolean showEmpty) {
		category.getViewModel()
				.setShowEmpty(showEmpty);
	}

	@BindingAdapter("android:showHeader")
	public static void bindShowHeader(ScrollViewCategory category,
									  Boolean showHeader) {
		category.getViewModel()
				.setShowHeader(showHeader);
	}

	@BindingAdapter("android:subcategoryGeneratorFunction")
	public static <T extends EntityViewModel<? extends BaseEntity>> void setSubcategoryGeneratorFunction(ScrollViewCategory<T> categoryView,
																										 Function<CategoryViewModel<T>, List<CategoryViewModel<T>>> subcategoryGeneratorFunction) {
		categoryView.addChildCategories(subcategoryGeneratorFunction
				.apply(categoryView.getViewModel()));
	}

	@BindingAdapter("android:subcategoryDynamicGeneratorFunction")
	public static <T extends EntityViewModel<? extends BaseEntity>> void setSubcategoryDynamicGeneratorFunction(ScrollViewCategory<T> categoryView,
																												Function<CategoryViewModel<T>, List<CategoryViewModel<T>>> subcategoryGeneratorFunction) {
		categoryView.getViewModel().setGeneratorFunction(subcategoryGeneratorFunction);
	}

	@BindingAdapter("android:permissionLevel")
	public static void setPermissionLevel(ScrollViewCategory category,
										  Integer permissionLevel) {
		category.getViewModel()
				.setPermissionLevel(permissionLevel);
	}

	@BindingAdapter("android:entityName")
	public static void setEntityName(ScrollViewCategory category,
									 String entityName) {
		category.getViewModel()
				.setEntityName(entityName);
	}

	@BindingAdapter("android:categoryTitle")
	public static void setCategoryTitle(ScrollViewCategory category, String title) {
		if (category != null && category.getViewModel() != null) {
			category.getViewModel()
					.setCategoryTitle(title);
		}
	}
}
