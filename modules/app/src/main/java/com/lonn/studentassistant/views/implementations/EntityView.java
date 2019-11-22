package com.lonn.studentassistant.views.implementations;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.EntityConstraintLayoutViewBinding;
import com.lonn.studentassistant.databinding.EntityTableRowViewBinding;
import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.models.Grade;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;
import com.lonn.studentassistant.viewModels.entities.ScrollViewEntityViewModel;
import com.lonn.studentassistant.views.abstractions.ScrollViewItem;

public class EntityView<T extends BaseEntity> extends ScrollViewItem<T> {
    protected String viewType;
    protected T entity;
    protected ScrollViewEntityViewModel model;
    protected ViewDataBinding binding;
    protected int permissionLevel = 0;

    public EntityView(Context context, T entity, String viewType, int permissionLevel) {
        super(context);

        this.entity = entity;
        this.viewType = viewType;
        this.permissionLevel = permissionLevel;

        init(context);

        if (entity != null) {
            if (viewType.equals("full")) {
                model = ScrollViewEntityViewModel.full(entity);
            }
            else {
                model = ScrollViewEntityViewModel.partial(entity);
            }

            model.permissionLevel = permissionLevel;
            setDataBindingVariable(model);
        }
    }

    public EntityView(Context context, AttributeSet set) {
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

    public void inflateLayout(final Context context) {
        binding = DataBindingUtil.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), getLayoutId(), this, true);
    }

    public void addOrUpdate(T newEntity) {
        entity = newEntity;

        if (viewType.equals("full")) {
            model = ScrollViewEntityViewModel.full(entity);
        }
        else {
            model = ScrollViewEntityViewModel.partial(entity);
        }

        model.permissionLevel = permissionLevel;
        setDataBindingVariable(model);
    }

    public void delete(T newEntity) {
    }

    public void delete(String key) {
    }

    public boolean shouldContain(T entity) {
        return true;
    }

    public Class getEntityClass() {
        return entity.getClass();
    }

    public T getEntity() {
        return entity;
    }

    public Class getActivityClass() {
        return model.entityActivityClass;
    }

    protected int getLayoutId() {
        if (entity instanceof ScheduleClass || entity instanceof Grade) {
            return R.layout.entity_table_row_view;
        }

        return R.layout.entity_constraint_layout_view;
    }

    private void setDataBindingVariable(ScrollViewEntityViewModel model) {
        if (entity instanceof Grade || entity instanceof ScheduleClass) {
            ((EntityTableRowViewBinding) binding).setEntity(model);
        }
        else {
            ((EntityConstraintLayoutViewBinding) binding).setEntity(model);
        }
    }
}
