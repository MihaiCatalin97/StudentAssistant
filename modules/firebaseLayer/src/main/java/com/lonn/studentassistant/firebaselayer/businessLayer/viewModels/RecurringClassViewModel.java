package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels;

import android.view.View;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.Utils;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.ScheduleClassViewModel;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class RecurringClassViewModel extends ScheduleClassViewModel<RecurringClass> {
	private int day;
	@Bindable
	public String parity;


	@Bindable
	public int getParityVisible() {
		return (parity != null && parity.length() > 0) ? View.VISIBLE : View.GONE;
	}

	@Bindable
	public String getDay() {
		return Utils.dayToString(day);
	}

	public int getDayInt(){
		return day;
	}

	@Override
	public RecurringClassViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}

	@Override
	public RecurringClassViewModel clone() {
		return (RecurringClassViewModel) super.clone();
	}
}
