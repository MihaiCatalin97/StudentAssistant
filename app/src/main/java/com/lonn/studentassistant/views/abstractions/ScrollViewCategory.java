package com.lonn.studentassistant.views.abstractions;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.entities.BaseEntity;

public abstract class ScrollViewCategory<T extends BaseEntity> extends ScrollViewItem<T>
{
    protected int categoryInt;
    protected LinearLayout categoryContentLayout;

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

        ((CheckBox)findViewById(R.id.checkBoxCategory)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    categoryContentLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    categoryContentLayout.setVisibility(View.GONE);
                }
            }
        });
    }


}
