package com.lonn.studentassistant.views.abstractions;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.lonn.studentassistant.entities.BaseEntity;

public abstract class ScrollViewItem<T extends BaseEntity> extends LinearLayout
{
    public ScrollViewItem(Context context)
    {
        super(context);
        init(context);
    }

    public ScrollViewItem(Context context, AttributeSet set)
    {
        super(context, set);
        init(context);
    }

    public ScrollViewItem(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollViewItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected abstract void inflateLayout(Context context);

    protected void init(Context context)
    {
        inflateLayout(context);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        setSoundEffectsEnabled(false);
    }

    public abstract void addOrUpdate(T newEntities);
    public abstract void delete(T newEntities);
}
