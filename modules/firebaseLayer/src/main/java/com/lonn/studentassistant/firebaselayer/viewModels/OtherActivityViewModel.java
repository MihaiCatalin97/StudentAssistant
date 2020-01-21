package com.lonn.studentassistant.firebaselayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.DisciplineViewModel;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class OtherActivityViewModel extends DisciplineViewModel<OtherActivity> {
	@Bindable
	private String type;

	@Override
	public OtherActivityViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}

	@Override
	public OtherActivityViewModel clone() {
		return (OtherActivityViewModel) super.clone();
	}
}
