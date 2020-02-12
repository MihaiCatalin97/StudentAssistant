package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;

import java.util.List;

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
	private String associatedEntityKey;
	private List<AccountType> targetedGroups;

	@Override
	public FileContentViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}

	@Override
	public FileContentViewModel clone() {
		return (FileContentViewModel) super.clone();
	}
}
