package com.lonn.studentassistant.views.abstractions.category;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.CategoryLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.viewModels.entities.CategoryViewModel;
import com.lonn.studentassistant.views.abstractions.ExpandAnimation;
import com.lonn.studentassistant.views.abstractions.ScrollViewItem;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static com.lonn.studentassistant.R.id.arrowCategory;
import static com.lonn.studentassistant.R.id.layoutCategoryContent;
import static com.lonn.studentassistant.R.id.layoutCategoryHeader;
import static com.lonn.studentassistant.R.id.layoutCategoryMain;
import static com.lonn.studentassistant.R.layout.category_layout;

public abstract class ScrollViewCategory<T extends BaseEntity> extends ScrollViewItem<T> {
	protected CategoryViewModel categoryViewModel = new CategoryViewModel();
	protected ScrollViewCategoryHeader categoryHeaderLayout;
	protected ScrollViewCategoryContent<T> categoryContentLayout;

	protected boolean expanded = false;

	public ScrollViewCategory(Context context) {
		super(context);
		init(context);
	}

	public ScrollViewCategory(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttributesFromSet(attrs);
		init(context);
	}

	public ScrollViewCategory(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getAttributesFromSet(attrs);
		init(context);
	}

	@BindingAdapter("srcCompat")
	public static void bindSrcCompat(ImageView imageView, int resourceId) {
		imageView.setImageResource(resourceId);
	}

	public CategoryViewModel getCategoryViewModel() {
		return categoryViewModel;
	}

	protected abstract void initCategoryViewModel();

	protected void initContent() {
		categoryContentLayout = findViewById(layoutCategoryMain).findViewById(layoutCategoryContent);
		categoryHeaderLayout = findViewById(layoutCategoryMain).findViewById(layoutCategoryHeader);

		categoryHeaderLayout.setOnClickListener(v -> {
			expanded = !expanded;
			animateExpand();
		});
	}

	protected abstract void generateChildCategories(T entity);

	private void getAttributesFromSet(AttributeSet set) {
		TypedArray a = getContext().obtainStyledAttributes(set, R.styleable.ScrollViewCategory);
		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i) {
			int attr = a.getIndex(i);
			switch (attr) {
				case R.styleable.ScrollViewCategory_category_title:
					categoryViewModel.categoryName = a.getString(attr);
					break;
				case R.styleable.ScrollViewCategory_show_base_header:
					categoryViewModel.showHeader = a.getBoolean(attr, true);
					break;
				case R.styleable.ScrollViewCategory_permission_level:
					categoryViewModel.permissionLevel = a.getInteger(attr, 0);
					break;
			}
		}
		a.recycle();
	}

	public ScrollViewCategoryContent<T> getCategoryContentLayout() {
		return categoryContentLayout;
	}

	private void animateExpand() {
		RotateAnimation animation =
				new RotateAnimation(expanded ? 0 : 180, expanded ? 180 : 360,
						RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);

		animation.setDuration(500);
		animation.setFillAfter(true);
		animation.setFillBefore(true);

		categoryHeaderLayout.findViewById(arrowCategory).startAnimation(animation);

		ExpandAnimation expandAnimation = new ExpandAnimation();
		expandAnimation.setDuration(500);
		expandAnimation.start(categoryContentLayout);
	}

	@Override
	public void addOrUpdate(T entity) {
		if (shouldContain(entity)) {
			categoryContentLayout.addEntityView(entity);
		}
	}

	@Override
	protected void inflateLayout(Context context) {
		LayoutInflater layoutInflater = (LayoutInflater)
				context.getSystemService(LAYOUT_INFLATER_SERVICE);
		if (layoutInflater != null) {
			CategoryLayoutBinding binding = DataBindingUtil.inflate(
					layoutInflater,
					category_layout,
					this,
					true);
			binding.setCategoryModel(categoryViewModel);
		}
	}

	@Override
	protected void init(Context context) {
		super.init(context);
		initContent();
		initCategoryViewModel();
	}
}
