package com.lonn.studentassistant.views.abstractions;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.entities.BaseEntity;

public abstract class ScrollViewCategory<T extends BaseEntity> extends ScrollViewItem<T>
{
    protected int categoryInt;
    protected View categoryHeaderLayout;
    protected LinearLayout categoryContentLayout;
    protected boolean expanded = false;

    public ScrollViewCategory(Context context)
    {
        super(context);
        init(context);
    }

    public ScrollViewCategory(Context context, AttributeSet set)
    {
        super(context, set);
        init(context);
    }

    public ScrollViewCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollViewCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public abstract void setCategory(int category);

    public int getCategory()
    {
        return categoryInt;
    }

    protected void initContent()
    {
        categoryContentLayout = findViewById(R.id.layoutCategoryContent);
        categoryHeaderLayout = findViewById(R.id.layoutCategoryHeader);

        categoryHeaderLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                expanded = !expanded;

                animateExpand();
            }
        });
    }

    private void animateExpand()
    {
        if(expanded)
        {
            RotateAnimation animation =
                    new RotateAnimation(0,180,
                            Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);

            animation.setDuration(500);
            animation.setFillAfter(true);
            animation.setFillBefore(true);

            categoryHeaderLayout.findViewById(R.id.arrowCategory).startAnimation(animation);
        }
        else
        {
            RotateAnimation animation =
                    new RotateAnimation(180,360,
                            Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);

            animation.setDuration(500);
            animation.setFillAfter(true);
            animation.setFillBefore(true);

            categoryHeaderLayout.findViewById(R.id.arrowCategory).startAnimation(animation);
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)categoryContentLayout.getLayoutParams();
        final int initialMargin = params.topMargin;

        if(categoryContentLayout.getMeasuredHeight() == 0 && categoryContentLayout.getChildCount()!=0)
        {
            params.topMargin = -100000;
            categoryContentLayout.setLayoutParams(params);
            categoryContentLayout.setVisibility(View.VISIBLE);

            categoryContentLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
            {
                @Override
                public boolean onPreDraw()
                {
                    int height = categoryContentLayout.getMeasuredHeight();

                    if (height != 0)
                    {
                        Ani ani = new Ani(categoryContentLayout, expanded, initialMargin);
                        ani.setDuration(500);
                        categoryContentLayout.startAnimation(ani);

                        categoryContentLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                    }

                    return true;
                }
            });
        }
        else
        {
            Ani ani = new Ani(categoryContentLayout, expanded, initialMargin);
            ani.setDuration(500);
            categoryContentLayout.startAnimation(ani);
        }
    }

    class Ani extends Animation {
        private boolean expanding;
        private int initialMarginTop, viewHeight;
        private View view;

        Ani(View view, boolean expanding, int initialMarginTop)
        {
            this.view = view;
            this.expanding = expanding;
            this.initialMarginTop = initialMarginTop;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newMargin;

            if(expanding)
                newMargin = (int)((-2*initialMarginTop - viewHeight) * (1 - interpolatedTime)) + initialMarginTop;
            else
                newMargin = (int)((-2*initialMarginTop - viewHeight) * interpolatedTime) + initialMarginTop;

            Log.e("New margin " + Boolean.toString(expanding), Integer.toString(newMargin));
            ((RelativeLayout.LayoutParams)view.getLayoutParams()).topMargin = newMargin;
            view.requestLayout();

            if(interpolatedTime == 1)
            {
                if(!expanding)
                {
                    view.setVisibility(View.GONE);
                    ((RelativeLayout.LayoutParams)view.getLayoutParams()).topMargin = initialMarginTop;
                }
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);

            view.setVisibility(View.VISIBLE);

            ((RelativeLayout.LayoutParams)view.getLayoutParams()).topMargin = initialMarginTop;
            viewHeight = height;
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    };
}
