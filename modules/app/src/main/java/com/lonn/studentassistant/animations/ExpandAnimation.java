package com.lonn.studentassistant.animations;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public class ExpandAnimation extends Animation {
    private boolean expanding;
    private int initialMarginTop, viewHeight;
    private ViewGroup view;

    public ExpandAnimation() {
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

        view.setVisibility(View.VISIBLE);

        ((RelativeLayout.LayoutParams) view.getLayoutParams()).topMargin = initialMarginTop;
        viewHeight = height;
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    public void start(final ViewGroup categoryContentLayout) {
        this.view = categoryContentLayout;
        this.expanding = categoryContentLayout.getVisibility() != View.VISIBLE;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) categoryContentLayout.getLayoutParams();
        final int initialMargin = params.topMargin;

        if (categoryContentLayout.getMeasuredHeight() == 0 && categoryContentLayout.getChildCount() != 0) {
            params.topMargin = -1000 * categoryContentLayout.getChildCount();
            categoryContentLayout.setLayoutParams(params);
            categoryContentLayout.setVisibility(View.VISIBLE);

            categoryContentLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    int height = categoryContentLayout.getMeasuredHeight();

                    if (height != 0) {
                        categoryContentLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        initialMarginTop = initialMargin;
                        view.startAnimation(ExpandAnimation.this);
                    }

                    return true;
                }
            });
        }
        else {
            initialMarginTop = initialMargin;
            view.startAnimation(ExpandAnimation.this);
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newMargin;

        if (expanding) {
            newMargin = (int) ((-2 * initialMarginTop - viewHeight) * (1 - interpolatedTime)) + initialMarginTop;
        }
        else {
            newMargin = (int) ((-2 * initialMarginTop - viewHeight) * interpolatedTime) + initialMarginTop;
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
}
