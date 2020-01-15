package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Laboratory extends BaseEntity {
	private String course;
	private List<String> grades;
	private List<String> filesMetadata = new LinkedList<>();
	private String description;
	private String title;
	private int weekNumber;
}
