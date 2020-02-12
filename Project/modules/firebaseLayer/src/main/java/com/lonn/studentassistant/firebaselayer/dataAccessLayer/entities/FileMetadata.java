package com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class FileMetadata extends BaseEntity {
	private String associatedEntityKey;
	private String fileName;
	private long fileSize;
	private String fileType;
	private String fileContentKey;
	private String fileTitle;
	private String fileDescription;
	private List<AccountType> targetedGroups = new LinkedList<>();
}
