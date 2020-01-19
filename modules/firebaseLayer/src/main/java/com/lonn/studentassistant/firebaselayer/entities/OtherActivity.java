package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.FileAssociatedEntity;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class OtherActivity extends Discipline {
	private String type;

	public OtherActivity setFileMetadataKeys(List<String> fileMetadataKeys) {
		this.fileMetadataKeys = fileMetadataKeys;
		return this;
	}
}
