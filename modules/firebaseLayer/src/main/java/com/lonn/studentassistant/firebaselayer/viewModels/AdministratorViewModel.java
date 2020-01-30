package com.lonn.studentassistant.firebaselayer.viewModels;

import androidx.databinding.Bindable;

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
	@Bindable
	private String firstName;
	@Bindable
	private String lastName;
	@Bindable
	private String email;
	@Bindable
	private String phoneNumber;
	@Bindable
	private String imageMetadataKey;

	@Override
	public AdministratorViewModel clone() {
		return (AdministratorViewModel) super.clone();
	}
}