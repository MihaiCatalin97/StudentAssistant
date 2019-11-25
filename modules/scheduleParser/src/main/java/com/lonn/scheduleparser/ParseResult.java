package com.lonn.scheduleparser;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;

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
