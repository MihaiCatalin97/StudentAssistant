package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class OneTimeClass extends ScheduleClass {
	private Date date;
}
