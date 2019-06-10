package com.lonn.studentassistant.views.abstractions;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.entities.BaseEntity;

public abstract class ScrollViewCategory<T extends BaseEntity> extends ScrollViewItem<T>
{
    protected String category;
    protected View categoryHeaderLayout;
    protected LinearLayout categoryContentLayout;
    protected boolean expanded = false;

    public ScrollViewCategory(Context context)
    {
        super(context);
    }

    public ScrollViewCategory(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public ScrollViewCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollViewCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setCategory(final String category)
    {
        this.category = category;

        ((TextView)findViewById(R.id.titleCategory)).setText(category);
    }

    @Override
    protected void inflateLayout(Context context)
    {
        inflate(context, R.layout.category_layout, this);
    }

    @Override
    protected void init(Context context)
    {
        super.init(context);
        initContent();
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
        RotateAnimation animation =
                new RotateAnimation(expanded?0:180,expanded?180:360,
                        Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setFillBefore(true);

        categoryHeaderLayout.findViewById(R.id.arrowCategory).startAnimation(animation);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)categoryContentLayout.getLayoutParams();
        final int initialMargin = params.topMargin;

        if(categoryContentLayout.getMeasuredHeight() == 0 && categoryContentLayout.getChildCount()!=0)
        {
            params.topMargin = -1000*categoryContentLayout.getChildCount();
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
                        expandCategory(initialMargin);
                        categoryContentLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                    }

                    return true;
                }
            });
        }
        else
        {
            expandCategory(initialMargin);
        }
    }

    private void expandCategory(int initialMargin)
    {
        ExpandAnimation ani = new ExpandAnimation(categoryContentLayout, expanded, initialMargin);
        ani.setDuration(500);
        categoryContentLayout.startAnimation(ani);

    }

    private class ExpandAnimation extends Animation
    {
        private boolean expanding;
        private int initialMarginTop, viewHeight;
        private View view;

        ExpandAnimation(View view, boolean expanding, int initialMarginTop)
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
    }
}
