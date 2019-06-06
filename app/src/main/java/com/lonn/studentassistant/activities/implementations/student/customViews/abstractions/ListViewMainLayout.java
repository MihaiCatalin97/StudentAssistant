package com.lonn.studentassistant.activities.implementations.student.customViews.abstractions;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.List;

public abstract class ListViewMainLayout<T extends BaseEntity> extends CustomView<T>
{
    protected LinearLayout categoryContentLayout;

    public ListViewMainLayout(Context context)
    {
        super(context);
    }

    public ListViewMainLayout(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public ListViewMainLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListViewMainLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public abstract void update(List<T> newCourses);
}
