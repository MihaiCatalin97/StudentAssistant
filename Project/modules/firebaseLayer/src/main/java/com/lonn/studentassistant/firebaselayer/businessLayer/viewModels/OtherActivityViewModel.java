package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.DisciplineViewModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
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

	public OtherActivityViewModel setName(String name) {
		this.name = name;
		return this;
	}

	public OtherActivityViewModel setDescription(String description) {
		this.description = description;
		return this;
	}

	public OtherActivityViewModel setWebsite(String website) {
		this.website = website;
		return this;
	}

	public OtherActivityViewModel setSemester(int semester) {
		this.semester = semester;
		return this;
	}
}
