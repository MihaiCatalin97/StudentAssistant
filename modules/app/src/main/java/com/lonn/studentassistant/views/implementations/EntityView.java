package com.lonn.studentassistant.views.implementations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.EntityConstraintLayoutViewBinding;
import com.lonn.studentassistant.databinding.EntityTableRowViewBinding;
import com.lonn.studentassistant.databinding.ScheduleTableRowViewBinding;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
import com.lonn.studentassistant.viewModels.ScrollViewEntityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;
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

	public void setOnDeleteTap(Consumer<T> onDeleteTap) {
		findViewById(R.id.entityRemoveButton).setOnClickListener((view) -> {
			if (onDeleteTap != null)
				onDeleteTap.consume(entityViewModel);
		});
	}

	public EntityView(Context context, AttributeSet set) {
		super(context, set);
		init(context);
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
		if (entityViewModel instanceof ScheduleClassViewModel) {
			return R.layout.schedule_table_row_view;
		}
		else if (entityViewModel instanceof GradeViewModel) {
			return R.layout.entity_table_row_view;
		}

		return R.layout.entity_constraint_layout_view;
	}

	private void setDataBindingVariable(ScrollViewEntityViewModel model) {
		if (entityViewModel instanceof ScheduleClassViewModel) {
			((ScheduleTableRowViewBinding) binding).setScheduleClass((ScheduleClassViewModel) entityViewModel);
		}
		else if (entityViewModel instanceof GradeViewModel) {
			((EntityTableRowViewBinding) binding).setEntityViewModel(model);
		}
		else {
			((EntityConstraintLayoutViewBinding) binding).setEntityViewModel(model);
		}
	}
}
