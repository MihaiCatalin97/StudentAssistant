package com.lonn.scheduleparser;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.RecurringClass;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ParseResult {
	private List<Professor> professors;
	private List<Course> courses;
	private List<OtherActivity> otherActivities;
	private List<OneTimeClass> oneTimeClasses;
	private List<RecurringClass> recurringClasses;
}
