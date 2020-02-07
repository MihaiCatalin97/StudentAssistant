package com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class Grade extends BaseEntity {
	private double value;
	private String studentId;
	private String studentKey;
	private String laboratoryKey;
	private String courseKey;
	private long date;
	private GradeType gradeType;
	private int laboratoryNumber;
}
