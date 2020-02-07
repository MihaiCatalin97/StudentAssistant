package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.ScheduleClassViewModel;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class OneTimeClassViewModel extends ScheduleClassViewModel<OneTimeClass> {
	@Bindable
	private Date date;

	@Override
	public OneTimeClassViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}

	@Override
	public OneTimeClassViewModel clone() {
		return (OneTimeClassViewModel) super.clone();
	}
}
