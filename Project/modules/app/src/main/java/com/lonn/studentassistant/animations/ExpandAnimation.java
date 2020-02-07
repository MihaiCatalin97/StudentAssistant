package com.lonn.studentassistant.animations;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public class ExpandAnimation extends Animation {
	private boolean expanding;
	private Integer initialMarginTop, initialMarginBottom, viewHeight;
	private ViewGroup view;

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);

		view.setVisibility(View.VISIBLE);

		((RelativeLayout.LayoutParams) view.getLayoutParams()).topMargin = initialMarginTop;
		viewHeight = getChildHeightSum(view);
	}

	@Override
	public boolean willChangeBounds() {
		return true;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		int newMargin;

		if (expanding) {
			newMargin = (int) ((-initialMarginTop - initialMarginBottom - viewHeight) * (1 - interpolatedTime)) + initialMarginTop;
		}
		else {
			newMargin = (int) ((-initialMarginTop - initialMarginBottom - viewHeight) * interpolatedTime) + initialMarginTop;
		}

		((RelativeLayout.LayoutParams) view.getLayoutParams()).topMargin = newMargin;
		view.requestLayout();

		if (interpolatedTime == 1) {
			if (!expanding) {
				view.setVisibility(View.GONE);
				((RelativeLayout.LayoutParams) view.getLayoutParams()).topMargin = initialMarginTop;
			}
		}
	}

	public void start(final ViewGroup categoryContentLayout) {
		this.view = categoryContentLayout;
		this.expanding = categoryContentLayout.getVisibility() != View.VISIBLE;

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) categoryContentLayout.getLayoutParams();
		initialMarginTop = params.topMargin;
		initialMarginBottom = params.bottomMargin;

		if (categoryContentLayout.getVisibility() != View.VISIBLE &&
				getChildHeightSum(categoryContentLayout) == 0 &&
				categoryContentLayout.getChildCount() != 0) {
			params.topMargin = -1000 * categoryContentLayout.getChildCount();
			categoryContentLayout.setLayoutParams(params);
			categoryContentLayout.setVisibility(View.VISIBLE);

			categoryContentLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
				@Override
				public boolean onPreDraw() {
					view.startAnimation(ExpandAnimation.this);
					categoryContentLayout.getViewTreeObserver().removeOnPreDrawListener(this);

					return true;
				}
			});
		}
		else {
			view.startAnimation(ExpandAnimation.this);
		}
	}

	private int getChildHeightSum(ViewGroup parent) {
		int heightSum = 0;

		for (int i = 0; i < parent.getChildCount(); i++) {
			heightSum += parent.getChildAt(i).getMeasuredHeight();
		}

		return heightSum;
	}
}
