package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.Person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class Administrator extends Person {
	public Administrator setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public Administrator setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public Administrator setEmail(String email) {
		this.email = email;
		return this;
	}

	public Administrator setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}
}
