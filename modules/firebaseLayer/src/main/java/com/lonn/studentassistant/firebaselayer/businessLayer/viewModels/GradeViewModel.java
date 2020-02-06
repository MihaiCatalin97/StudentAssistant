package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Grade;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;

import java.util.Date;

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
public final class GradeViewModel extends EntityViewModel<Grade> {
	private String studentId;
	private String studentKey;
	private double grade;
	private String laboratoryKey;
	private String courseKey;
	private Date date;
	private GradeType gradeType;
	private int laboratoryNumber;

	@Override
	public GradeViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}

	@Override
	public GradeViewModel clone() {
		return (GradeViewModel) super.clone();
	}
}
