package com.lonn.studentassistant.firebaselayer.viewModels;

import com.lonn.studentassistant.firebaselayer.entities.Administrator;
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
public final class AdministratorViewModel extends EntityViewModel<Administrator> {
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;

	@Override
	public AdministratorViewModel clone() {
		return (AdministratorViewModel) super.clone();
	}
}