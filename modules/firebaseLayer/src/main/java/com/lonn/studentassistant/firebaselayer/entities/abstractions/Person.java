package com.lonn.studentassistant.firebaselayer.entities.abstractions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public abstract class Person extends FileAssociatedEntity{
	protected String firstName;
	protected String lastName;
	protected String email;
	protected String phoneNumber;
	protected String imageMetadataKey;
}
