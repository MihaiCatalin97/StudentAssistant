package com.lonn.studentassistant.views.abstractions;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;

public abstract class ScrollViewLayout<T extends BaseEntity> extends ScrollViewItem<T>
{
    protected LinearLayout categoryContentLayout;

    public ScrollViewLayout(Context context)
    {
        super(context);
    }

    public ScrollViewLayout(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public ScrollViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollViewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public abstract void update(List<T> newCourses);
}
