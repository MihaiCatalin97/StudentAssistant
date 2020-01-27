package com.lonn.studentassistant.firebaselayer.entities;

import androidx.annotation.NonNull;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class RecurringClass extends ScheduleClass {
	private int day;
	private String parity;

	@Override
	@NonNull
	public String toString() {
		return getDay() + " " + getStartHour() + " " + getEndHour() + " " + getRooms().get(0);
	}
}
