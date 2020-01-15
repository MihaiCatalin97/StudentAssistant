package com.lonn.studentassistant.firebaselayer.viewModels;

import com.lonn.studentassistant.firebaselayer.entities.Laboratory;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LaboratoryViewModel extends EntityViewModel<Laboratory> {
	public String courseKey;
	public List<String> gradeKeys;
	public List<String> fileMetadataKeys;
	public String description;
	public String title;
	public int weekNumber;

	@Override
	public LaboratoryViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}
}
