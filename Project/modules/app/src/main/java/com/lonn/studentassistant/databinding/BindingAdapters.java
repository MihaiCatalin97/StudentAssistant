package com.lonn.studentassistant.databinding;

import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.functionalIntefaces.Comparator;
import com.lonn.studentassistant.functionalIntefaces.Function;
import com.lonn.studentassistant.functionalIntefaces.Predicate;
import com.lonn.studentassistant.viewModels.CategoryViewModel;
import com.lonn.studentassistant.views.EntityViewType;
import com.lonn.studentassistant.views.implementations.EntityView;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;

import java.util.Collection;
import java.util.List;

import static android.util.Base64.DEFAULT;
import static android.util.Base64.decode;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;
import static com.lonn.studentassistant.BR._all;

public class BindingAdapters {
	@BindingAdapter(value = {"android:layout_marginEnd", "android:layout_marginStart",
			"android:layout_marginLeft", "android:layout_marginRight",
			"android:layout_marginTop",
			"android:layout_marginBottom",
			"android:layout_height",
			"android:layout_width"}, requireAll = false)
	public static void setLayoutDip(View view, float marginEnd, float marginStart,
									float marginLeft, float marginRight,
									float marginTop,
									float marginBottom,
									int height,
									int width) {
		int leftMargin = marginLeft == 0 ? (int) marginStart : (int) marginLeft;
		int rightMargin = marginRight == 0 ? (int) marginEnd : (int) marginRight;

		if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();

			ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

			layoutParams.rightMargin = rightMargin;
			layoutParams.leftMargin = leftMargin;
			layoutParams.topMargin = (int) marginTop;
			layoutParams.bottomMargin = (int) marginBottom;

			if (height > 0) {
				layoutParams.height = (int) applyDimension(COMPLEX_UNIT_DIP, height, displayMetrics);
			}
			if (width > 0) {
				layoutParams.width = (int) applyDimension(COMPLEX_UNIT_DIP, width, displayMetrics);
			}

			view.setLayoutParams(layoutParams);
		}
	}

	@BindingAdapter(value = {"android:layout_marginStartPixels",
			"android:layout_marginTopPixels"}, requireAll = false)
	public static void setMarginsPixels(View view, float marginStartPixels,
										float marginTopPixels) {
		if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();

			ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

			layoutParams.leftMargin = (int) applyDimension(COMPLEX_UNIT_DIP, marginStartPixels, displayMetrics);
			layoutParams.topMargin = (int) applyDimension(COMPLEX_UNIT_DIP, marginTopPixels, displayMetrics);

			view.setLayoutParams(layoutParams);
		}
	}

	@BindingAdapter("android:layout_weight")
	public static void bindDeletable(View view,
									 float weight) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
		params.weight = weight;
		view.setLayoutParams(params);
	}

	@BindingAdapter("cardCornerRadius")
	public static void setCardCornerRadius(CardView cardView, int radius) {
		DisplayMetrics displayMetrics = cardView.getContext().getResources().getDisplayMetrics();
		cardView.setRadius(applyDimension(COMPLEX_UNIT_DIP, radius, displayMetrics));
	}

	@BindingAdapter("srcCompat")
	public static void setSrcCompat(ImageView imageView, int resourceId) {
		imageView.setImageResource(resourceId);
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

	@BindingAdapter(value = {"srcCompat", "defaultSrcCompat"}, requireAll = false)
	public static void setSrcCompat(ImageView imageView, String base64Resource,
									int resourceId) {
		if (base64Resource == null) {
			imageView.setImageResource(resourceId);
			return;
		}

		byte[] decodedImage = decode(base64Resource, DEFAULT);
		imageView.setImageBitmap(BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length));
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
		if (category != null) {
			if (entities != null &&
					category.getViewModel().getGeneratorFunction() != null) {
				category.setChildCategories(category.getViewModel().getGeneratorFunction()
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

	@BindingAdapter("android:canAdd")
	public static void setCanAdd(ScrollViewCategory category,
								 Boolean canAdd) {
		category.setCanAdd(canAdd);
	}

	@BindingAdapter("android:showAddButtonFirstLayer")
	public static void setshowAddButtonFirstLayer(ScrollViewCategory category,
												  Boolean showAddButtonFirstLayer) {
		if (showAddButtonFirstLayer != null && showAddButtonFirstLayer) {
			category.setShowAddButtonFirstLayer(showAddButtonFirstLayer);
		}
	}

	@BindingAdapter("android:shouldContain")
	public static <T extends EntityViewModel<? extends BaseEntity>> void setShouldContain(ScrollViewCategory<T> category,
																						  Predicate<T> shouldContain) {
		category.getViewModel()
				.setShouldContain(shouldContain);
	}

	@BindingAdapter("android:viewType")
	public static <T extends EntityViewModel<? extends BaseEntity>> void setViewType(ScrollViewCategory<T> category,
																					 EntityViewType type) {
		Consumer<ScrollViewCategory<T>> viewTypeRecursiveSetter = new Consumer<ScrollViewCategory<T>>() {
			@Override
			public void consume(ScrollViewCategory<T> scrollViewCategory) {
				scrollViewCategory.getViewModel()
						.setViewType(type);

				for (CategoryViewModel categoryViewModel : scrollViewCategory.getViewModel().getChildCategories()) {
					categoryViewModel.setViewType(type);
				}

				scrollViewCategory.getViewModel()
						.notifyPropertyChanged(_all);

				for (ScrollViewCategory<T> subcategory : scrollViewCategory.getContent().getSubcategories()) {
					this.consume(subcategory);
				}
			}
		};

		viewTypeRecursiveSetter.consume(category);
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

	@BindingAdapter("android:editing")
	public static void bindEditing(ScrollViewCategory category,
								   Boolean editing) {
		if (editing == null) {
			editing = false;
		}

		category.setEditing(editing);
	}

	@BindingAdapter("android:canApprove")
	public static void bindCanApprove(ScrollViewCategory category,
									  Boolean canApprove) {
		if (canApprove == null) {
			canApprove = false;
		}

		category.setCanApprove(canApprove);
	}

	@BindingAdapter("android:unlinkable")
	public static void bindUnlinkable(ScrollViewCategory category,
									  Boolean unlinkable) {
		if (unlinkable == null) {
			unlinkable = false;
		}

		category.setUnlinkable(unlinkable);
	}

	@BindingAdapter("android:deletable")
	public static void bindDeletable(ScrollViewCategory category,
									 Boolean deletable) {
		if (deletable == null) {
			deletable = false;
		}

		category.setDeletable(deletable);
	}

	@BindingAdapter("android:subcategoryGeneratorFunction")
	public static <T extends EntityViewModel<? extends BaseEntity>> void setSubcategoryGeneratorFunction(ScrollViewCategory<T> categoryView,
																										 Function<CategoryViewModel<T>, List<CategoryViewModel<T>>> subcategoryGeneratorFunction) {
		categoryView.setChildCategories(subcategoryGeneratorFunction
				.apply(categoryView.getViewModel()));
	}

	@BindingAdapter("android:subcategoryDynamicGeneratorFunction")
	public static <T extends EntityViewModel<? extends BaseEntity>> void setSubcategoryDynamicGeneratorFunction(ScrollViewCategory<T> categoryView,
																												Function<CategoryViewModel<T>, List<CategoryViewModel<T>>> subcategoryGeneratorFunction) {
		categoryView.getViewModel().setGeneratorFunction(subcategoryGeneratorFunction);

		List<CategoryViewModel<T>> subcategories =
				subcategoryGeneratorFunction.apply(categoryView.getViewModel());

		if (subcategories != null && subcategories.size() != 0) {
			categoryView.setChildCategories(subcategories);
		}
	}

	@BindingAdapter("android:permissionLevel")
	public static void setPermissionLevel(ScrollViewCategory category,
										  PermissionLevel permissionLevel) {
		category.setPermissionLevel(permissionLevel);
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
