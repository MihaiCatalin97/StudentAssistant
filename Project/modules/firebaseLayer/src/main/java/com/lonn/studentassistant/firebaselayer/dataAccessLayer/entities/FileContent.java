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
public final class FileContent extends BaseEntity {
	private String fileMetadataKey;
	private String fileContentBase64;
	private String associatedEntityKey;
	private List<AccountType> targetedGroups = new LinkedList<>();
}
