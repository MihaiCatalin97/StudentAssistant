package com.lonn.studentassistant.firebaselayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class OneTimeClassViewModel extends ScheduleClassViewModel<OneTimeClass> {
	@Bindable
	private Date date;

	@Override
	public OneTimeClassViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}
}
