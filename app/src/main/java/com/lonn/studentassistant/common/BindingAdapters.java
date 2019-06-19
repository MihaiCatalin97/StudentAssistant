package com.lonn.studentassistant.common;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;

public class BindingAdapters
{
    @BindingAdapter("android:layout_marginBottom")
    public static void setLayoutMarginBottom(View view, float margin)
    {
        if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.bottomMargin = (int) margin;
            view.setLayoutParams(layoutParams);
        }
    }
    @BindingAdapter("android:layout_marginLeft")
    public static void setLayoutMarginLeft(View view, float margin)
    {
        if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.leftMargin = (int) margin;
            view.setLayoutParams(layoutParams);
        }
    }
    @BindingAdapter("android:layout_marginTop")
    public static void setLayoutMarginTop(View view, float margin)
    {
        if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.topMargin = (int) margin;
            view.setLayoutParams(layoutParams);
        }
    }
    @BindingAdapter("android:layout_marginRight")
    public static void setLayoutMarginRight(View view, float margin)
    {
        if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.rightMargin = (int) margin;
            view.setLayoutParams(layoutParams);
        }
    }
}
