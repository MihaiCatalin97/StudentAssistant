package com.lonn.studentassistant.firebaselayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.PersonViewModel;

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
public final class AdministratorViewModel extends PersonViewModel<Administrator> {
	@Bindable
	private String firstName;
	@Bindable
	private String lastName;
	@Bindable
	private String email;
	@Bindable
	private String phoneNumber;
	@Bindable
	private List<String> fileMetadataKeys;

	@Override
	public AdministratorViewModel clone() {
		return (AdministratorViewModel) super.clone();
	}

	public AdministratorViewModel setImageMetadataKey(String imageMetadataKey) {
		this.imageMetadataKey = imageMetadataKey;

		return this;
	}

	@Override
	public AdministratorViewModel setKey(String key) {
		super.setKey(key);

		return this;
	}
}