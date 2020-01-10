package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Grade extends BaseEntity {
	private int value;
	private Date date;
	private String studentId;
	private String courseId;
}
