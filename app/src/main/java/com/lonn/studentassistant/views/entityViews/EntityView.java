package com.lonn.studentassistant.views.entityViews;

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

import com.google.common.base.CaseFormat;
import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.courseEntity.CourseEntityActivity;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.databinding.CourseViewFullBinding;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.viewModels.CourseViewModel;
import com.lonn.studentassistant.viewModels.EntityViewModel;

import java.lang.reflect.Method;


public class EntityView<T extends BaseEntity> extends LinearLayout
{
    protected String viewType;
    protected T entity;
    protected EntityViewModel<T> model;
    protected ViewDataBinding binding;

    public EntityView(Context context, T entity, String viewType)
    {
        super(context);

        this.entity = entity;
        this.viewType = viewType;

        init(context);

        if(entity != null)
        {
            model = getEntityViewModel(entity);
            setDatabindingVariable(model);
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

    public void init(Context context)
    {
        inflateLayout(context);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        setSoundEffectsEnabled(false);
    }

    @SuppressWarnings("unchecked")
    public void update(T newEntity)
    {
        entity = newEntity;

        if (model == null)
        {
            model = getEntityViewModel(newEntity);
            setDatabindingVariable(model);
        }
        else
        {
            model.update(newEntity);
        }

        model.notifyPropertyChanged(BR._all);
    }

    // stuff for non-abstract generics to work
    protected void setDatabindingVariable(EntityViewModel<T> model)
    {
        try
        {
            String upperFirstType = viewType.substring(0, 1).toUpperCase() + viewType.substring(1);
            String databindingClassName = CourseViewFullBinding.class.getName()
                    .replace("Course", entity.getClass().getSimpleName())
                    .replace("Full", upperFirstType)
                    .replace("Partial", upperFirstType);

            Class<?> databindingClass = Class.forName(databindingClassName);
            Method setEntityMethod = databindingClass.getDeclaredMethod("set" + entity.getClass().getSimpleName(), getViewModelClass());
            setEntityMethod.invoke(binding, model);
        }
        catch (Exception e)
        {
            Log.e("setDatabindingVariable", e.getLocalizedMessage());
        }
    }

    @SuppressWarnings("unchecked")
    protected EntityViewModel<T> getEntityViewModel(T entity)
    {
        try
        {
            Class<?> viewModelClass = getViewModelClass();

            if(viewModelClass != null)
            {
                Object newInstance = viewModelClass.getConstructor(entity.getClass()).newInstance(entity);

                if(newInstance instanceof EntityViewModel)
                    return (EntityViewModel<T>)newInstance;
            }

            return null;
        }
        catch (Exception e)
        {
            Log.e("getEntityViewModel", e.getLocalizedMessage());
            return null;
        }
    }

    private Class getViewModelClass()
    {
        try
        {
            return Class.forName(CourseViewModel.class.getName().replace("Course", entity.getClass().getSimpleName()));
        }
        catch (Exception e)
        {
            Log.e("getViewModelClass", e.getLocalizedMessage());
        }

        return null;
    }

    protected int getLayoutId()
    {
        return Utils.getId(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entity.getClass().getSimpleName()) + "_view_" + viewType, R.layout.class);
    }

    protected Class getActivityClass()
    {
        try
        {
            String entityClass = entity.getClass().getSimpleName();
            String classAux = CourseEntityActivity.class.getName();
            classAux = classAux.replace("Course", entityClass).replace("course", entityClass.toLowerCase());

            return Class.forName(classAux);
        }
        catch (Exception e)
        {
            Log.e("getActivityClass", e.getLocalizedMessage());
            return null;
        }
    }
}
