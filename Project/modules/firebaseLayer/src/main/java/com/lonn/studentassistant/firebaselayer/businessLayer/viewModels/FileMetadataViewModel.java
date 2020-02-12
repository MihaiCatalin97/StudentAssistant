package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels;

import android.webkit.MimeTypeMap;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.FileUtils;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.FileMetadata;
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
public final class FileMetadataViewModel extends EntityViewModel<FileMetadata> {
	private String fileContentKey;
	private String associatedEntityKey;
	@Bindable
	private String fileName;
	@Bindable
	private String fileTitle;
	@Bindable
	private String fileDescription;
	private long fileSize;
	@Bindable
	private String fileType;
	@Bindable
	private List<AccountType> targetedGroups;

	@Bindable
	public String getFileSize() {
		return FileUtils.sizeToString(fileSize);
	}

	public long getFileSizeLong(){
		return fileSize;
	}

	public String getFileDescription() {
		return fileDescription != null && fileDescription.length() != 0 ?
				fileDescription : "(no description)";
	}

	@Override
	public FileMetadataViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}

	@Bindable
	public String getFullFileName() {
		return fileName + "." + getFileExtension();
	}

	@Builder
	public String getFileExtension() {
		return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType);
	}

	@Override
	public FileMetadataViewModel clone() {
		return (FileMetadataViewModel) super.clone();
	}
}
