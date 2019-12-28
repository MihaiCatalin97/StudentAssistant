package com.lonn.studentassistant.views.abstractions;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;

public abstract class ScrollViewItem extends LinearLayout {
    public ScrollViewItem(Context context) {
        super(context);
    }

    public ScrollViewItem(Context context, AttributeSet set) {
        super(context, set);
    }

    protected abstract void inflateLayout(Context context);

    protected void init(Context context) {
        inflateLayout(context);
        setSoundEffectsEnabled(false);
    }
}
