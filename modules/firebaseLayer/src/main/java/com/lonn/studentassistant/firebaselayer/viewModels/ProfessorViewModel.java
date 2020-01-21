package com.lonn.studentassistant.firebaselayer.viewModels;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.FileAssociatedEntityViewModel;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProfessorViewModel extends FileAssociatedEntityViewModel<Professor> {
	@Bindable
	private String firstName, lastName, rank, email, phoneNumber, website, cabinet;
	@Bindable
	private String professorImage;
	private String imageMetadataKey;
	private List<String> courses;
	private List<String> otherActivities;
	private List<String> oneTimeClasses;
	private List<String> recurringClasses;

	@Bindable
	public String getFullName() {
		StringBuilder result = new StringBuilder();

		if (rank != null && rank.length() > 1) {
			result.append(rank).append(" ");
		}
		if (lastName != null && lastName.length() > 1) {
			result.append(lastName).append(" ");
		}
		if (firstName != null && firstName.length() > 1) {
			result.append(firstName).append(" ");
		}

		return result.toString();
	}

	@Bindable
	public int getEmailVisible() {
		return (email != null && email.contains("@")) ? View.VISIBLE : View.GONE;
	}

	@Bindable
	public int getWebsiteVisible() {
		return (website != null) ? View.VISIBLE : View.GONE;
	}

	@Bindable
	public int getPhoneNumberVisible() {
		return (phoneNumber != null && phoneNumber.length() >= 10) ? View.VISIBLE : View.GONE;
	}

	@Bindable
	public int getCoursesVisible() {
		return (courses != null && courses.size() > 0) ? View.VISIBLE : View.GONE;
	}

	@Override
	public ProfessorViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}

	public ProfessorViewModel setFileMetadataKeys(List<String> fileMetadataKeys) {
		this.fileMetadataKeys = fileMetadataKeys;
		return this;
	}

	@NonNull
	@Override
	public ProfessorViewModel clone() {
		return (ProfessorViewModel) super.clone();
	}
}
