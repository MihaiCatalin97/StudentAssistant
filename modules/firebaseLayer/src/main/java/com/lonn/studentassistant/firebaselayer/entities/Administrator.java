package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.HashableEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class Administrator extends BaseEntity implements HashableEntity {
	private String firstName;
	private String lastName;
	private String administratorKey;

	@Override
	public String computeHashingString() {
		return getFirstName() + getLastName() + getAdministratorKey();
	}
}
