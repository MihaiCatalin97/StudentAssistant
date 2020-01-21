package com.lonn.studentassistant.firebaselayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.Laboratory;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.FileAssociatedEntityViewModel;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class LaboratoryViewModel extends FileAssociatedEntityViewModel<Laboratory> {
	private String courseKey;
	private List<String> gradeKeys;
	private String description;
	private String title;
	private int weekNumber;

	@Override
	public LaboratoryViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}

	public LaboratoryViewModel setFileMetadataKeys(List<String> fileMetadataKeys) {
		this.fileMetadataKeys = fileMetadataKeys;
		return this;
	}

	@Bindable
	public String getWeek() {
		return Integer.toString(weekNumber);
	}

	public void setWeek(String week) {
		try {
			this.weekNumber = Integer.parseInt(week);
		}
		catch (NumberFormatException exception) {
			this.weekNumber = 0;
		}
	}

	@Override
	public LaboratoryViewModel clone() {
		return (LaboratoryViewModel) super.clone();
	}
}
