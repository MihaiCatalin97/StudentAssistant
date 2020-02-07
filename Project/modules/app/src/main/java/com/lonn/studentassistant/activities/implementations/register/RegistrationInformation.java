package com.lonn.studentassistant.activities.implementations.register;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RegistrationInformation extends BaseEntity {
	private String email;
	private String password;
	private String repeatPassword;
}
