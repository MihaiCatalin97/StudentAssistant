package com.lonn.studentassistant.views.implementations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.EntityConstraintLayoutViewBinding;
import com.lonn.studentassistant.databinding.EntityTableRowViewBinding;
import com.lonn.studentassistant.databinding.GradeLaboratoryTableRowBinding;
import com.lonn.studentassistant.databinding.OneTimeClassTableRowBinding;
import com.lonn.studentassistant.databinding.RecurringClassTableRowBinding;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;
import com.lonn.studentassistant.viewModels.ScrollViewEntityViewModel;
import com.lonn.studentassistant.views.EntityViewType;
import com.lonn.studentassistant.views.abstractions.ScrollViewItem;

import lombok.Getter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.lonn.studentassistant.viewModels.ScrollViewEntityViewModelFactory.getScrollViewEntityViewModel;

public class EntityView<T extends EntityViewModel> extends ScrollViewItem {
	protected EntityViewType viewType;
	@Getter
	protected T entityViewModel;
	@Getter
	protected ScrollViewEntityViewModel model;
	protected ViewDataBinding binding;
	protected int permissionLevel = 0;

	public EntityView(Context context, EntityViewType viewType, int permissionLevel,
					  T entityViewModel) {
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

	public void setOnDeleteTap(Consumer<T> onDeleteTap) {
		findViewById(R.id.entityRemoveButton).setOnClickListener((view) -> {
			if (onDeleteTap != null)
				onDeleteTap.consume(entityViewModel);
		});
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

	protected int getLayoutId() {
		if (entityViewModel instanceof RecurringClassViewModel) {
			return R.layout.one_time_class_table_row;
		}
		if (entityViewModel instanceof OneTimeClassViewModel) {
			return R.layout.recurring_class_table_row;
		}
		if (entityViewModel instanceof GradeViewModel) {
			return R.layout.grade_laboratory_table_row;
		}

		return R.layout.entity_constraint_layout_view;
	}

	private void setDataBindingVariable(ScrollViewEntityViewModel model) {
		if (entityViewModel instanceof RecurringClassViewModel) {
			((OneTimeClassTableRowBinding) binding).setScheduleClass((ScheduleClassViewModel) entityViewModel);
		}
		else if (entityViewModel instanceof OneTimeClassViewModel) {
			((RecurringClassTableRowBinding) binding).setScheduleClass((ScheduleClassViewModel) entityViewModel);
		}
		else if (entityViewModel instanceof GradeViewModel) {
			((GradeLaboratoryTableRowBinding) binding).setGrade((GradeViewModel) entityViewModel);
		}
		else {
			((EntityConstraintLayoutViewBinding) binding).setEntityViewModel(model);
		}
	}
}
