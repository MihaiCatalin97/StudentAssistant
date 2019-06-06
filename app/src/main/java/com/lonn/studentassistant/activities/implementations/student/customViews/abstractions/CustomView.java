package com.lonn.studentassistant.activities.implementations.student.customViews.abstractions;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;

public abstract class CustomView<T extends BaseEntity> extends LinearLayout
{
    public CustomView(Context context)
    {
        super(context);
        init(context);
    }

    public CustomView(Context context, AttributeSet set)
    {
        super(context, set);
        init(context);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public abstract void inflateLayout(Context context);

    public void init(Context context)
    {
        inflateLayout(context);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        setSoundEffectsEnabled(false);
    }

    public abstract void update(List<T> newEntities);
}
