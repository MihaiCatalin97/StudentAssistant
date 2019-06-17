package com.lonn.studentassistant.views.implementations.entityViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.common.base.CaseFormat;
import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.courseEntity.CourseEntityActivity;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.databinding.CourseViewFullBinding;
import com.lonn.studentassistant.databinding.EntityConstraintLayoutViewBinding;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.entities.Grade;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.viewModels.CourseViewModel;
import com.lonn.studentassistant.viewModels.EntityViewModel;
import com.lonn.studentassistant.views.abstractions.ScrollViewItem;

import java.lang.reflect.Method;


public abstract class EntityView<T extends BaseEntity> extends ScrollViewItem<T>
{
    protected T entity;
    protected EntityViewModel<T> model;
    protected ViewDataBinding binding;

    public EntityView(Context context, T entity)
    {
        super(context);

        this.entity = entity;

        init(context);

        if(entity != null)
        {
            model = getEntityViewModel(entity);
            setDataBindingVariable(model);
        }
    }

    public EntityView(Context context, AttributeSet set)
    {
        super(context, set);
        init(context);
    }

    public EntityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EntityView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void inflateLayout(final Context context)
    {
        binding = DataBindingUtil.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), getLayoutId(), this, true);

        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, getActivityClass());

                intent.putExtra("entity", entity);

                context.startActivity(intent);
            }
        });
    }

    public void addOrUpdate(T newEntity)
    {
        entity = newEntity;

        if (model == null)
        {
            model = getEntityViewModel(newEntity);
            setDataBindingVariable(model);
        }
        else
        {
            model.update(newEntity);
            model.notifyPropertyChanged(BR._all);
        }
    }

    public void delete(T newEntity)
    { }

    public boolean shouldContain(T entity)
    {
        return true;
    }

    protected abstract EntityViewModel<T> getEntityViewModel(T entity);

    protected int getLayoutId()
    {
        if(entity instanceof ScheduleClass || entity instanceof Grade)
            return R.layout.entity_constraint_layout_view;

        return R.layout.entity_constraint_layout_view;
    }

    protected abstract Class getActivityClass();

    private void setDataBindingVariable(EntityViewModel<T> model)
    {
        if(entity instanceof Grade || entity instanceof ScheduleClass)
            ((EntityConstraintLayoutViewBinding) binding).setEntity(model);
        else
            ((EntityConstraintLayoutViewBinding) binding).setEntity(model);
    }
}
