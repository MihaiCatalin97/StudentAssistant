package com.lonn.studentassistant.viewModels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
public class ScrollViewEntityViewModel extends BaseObservable {
	@Getter
	public Class entityActivityClass;
	@Bindable
	public int permissionLevel;
	@Bindable
	public String field1, field2, field3, field4, field5, image;

	@Bindable
	public int getField2LineCount() {
		if (field2 != null) {
			return field2.split("\n").length;
		}
		return 0;
	}

	@Bindable
	public int getField3LineCount() {
		if (field3 != null) {
			return field3.split("\n").length;
		}
		return 0;
	}

	@Bindable
	public int getField4LineCount() {
		if (field4 != null) {
			return field4.split("\n").length;
		}
		return 0;
	}
}
