package com.lonn.studentassistant.firebaselayer.viewModels;

import android.webkit.MimeTypeMap;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.FileUtils;
import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
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
public class FileMetadataViewModel extends EntityViewModel<FileMetadata> {
	public String fileContentKey;
	@Bindable
	public String fileName;
	@Bindable
	public String fileTitle;
	@Bindable
	public String fileDescription;
	public long fileSize;
	@Bindable
	public String fileType;

	@Bindable
	public String getFileSize() {
		return FileUtils.sizeToString(fileSize);
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
}
