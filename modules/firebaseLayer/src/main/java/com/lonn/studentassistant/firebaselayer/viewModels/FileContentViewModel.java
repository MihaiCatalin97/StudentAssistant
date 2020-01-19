package com.lonn.studentassistant.firebaselayer.viewModels;

import com.lonn.studentassistant.firebaselayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

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
public final class FileContentViewModel extends EntityViewModel<FileContent> {
	private String fileMetadataKey;
	private String fileContentBase64;

	@Override
	public FileContentViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}
}
