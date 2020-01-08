package com.lonn.studentassistant.firebaselayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.Utils;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RecurringClassViewModel extends ScheduleClassViewModel<RecurringClass> {
	public int day;

	@Bindable
	public String getDay() {
		return Utils.dayToString(day);
	}
}
