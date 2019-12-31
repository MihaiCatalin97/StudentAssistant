package com.lonn.studentassistant.views.implementations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.EntityConstraintLayoutViewBinding;
import com.lonn.studentassistant.databinding.EntityTableRowViewBinding;
import com.lonn.studentassistant.databinding.RecurringClassTableRowViewBinding;
import com.lonn.studentassistant.viewModels.ScrollViewEntityViewModel;
import com.lonn.studentassistant.viewModels.entities.GradeViewModel;
import com.lonn.studentassistant.viewModels.entities.RecurringClassViewModel;
import com.lonn.studentassistant.viewModels.entities.abstractions.EntityViewModel;
import com.lonn.studentassistant.viewModels.entities.abstractions.ScheduleClassViewModel;
import com.lonn.studentassistant.views.EntityViewType;
import com.lonn.studentassistant.views.abstractions.ScrollViewItem;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.lonn.studentassistant.viewModels.ScrollViewEntityViewModelFactory.getScrollViewEntityViewModel;

public class EntityView<T extends EntityViewModel> extends ScrollViewItem {
    protected EntityViewType viewType;
    protected T entityViewModel;
    protected ScrollViewEntityViewModel model;
    protected ViewDataBinding binding;
    protected int permissionLevel = 0;

    public EntityView(Context context, EntityViewType viewType, int permissionLevel, T entityViewModel) {
        super(context);

        this.viewType = viewType;
        this.permissionLevel = permissionLevel;
        this.entityViewModel = entityViewModel;

        init(context);
        updateEntity(entityViewModel);
    }

    public EntityView(Context context, AttributeSet set) {
        super(context, set);
        init(context);
    }

    public ScrollViewEntityViewModel getModel() {
        return model;
    }

    public void inflateLayout(final Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), this, true);
        }
    }

    public void updateEntity(T newEntity) {
        entityViewModel = newEntity;

        model = getScrollViewEntityViewModel(viewType, entityViewModel);
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
        return entityViewModel.getClass();
    }

    public T getEntityViewModel() {
        return entityViewModel;
    }

    public Class getActivityClass() {
        return model.entityActivityClass;
    }

    protected int getLayoutId() {
        if (entityViewModel instanceof RecurringClassViewModel) {
            return R.layout.recurring_class_table_row_view;
        }
        else if (entityViewModel instanceof ScheduleClassViewModel || entityViewModel instanceof GradeViewModel) {
            return R.layout.entity_table_row_view;
        }

        return R.layout.entity_constraint_layout_view;
    }

    private void setDataBindingVariable(ScrollViewEntityViewModel model) {
        if (entityViewModel instanceof RecurringClassViewModel) {
            ((RecurringClassTableRowViewBinding) binding).setRecurringClass((RecurringClassViewModel) entityViewModel);
        }
        else if (entityViewModel instanceof ScheduleClassViewModel || entityViewModel instanceof GradeViewModel) {
            ((EntityTableRowViewBinding) binding).setEntityViewModel(model);
        }
        else {
            ((EntityConstraintLayoutViewBinding) binding).setEntityViewModel(model);
        }
    }
}
