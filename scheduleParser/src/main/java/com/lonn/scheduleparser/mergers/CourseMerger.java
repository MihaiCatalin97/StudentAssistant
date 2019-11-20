package com.lonn.scheduleparser.mergers;

import com.lonn.studentassistant.firebaselayer.models.Course;

public class CourseMerger extends Merger<Course> {
    protected boolean mergingCondition(Course course1, Course course2) {
        return course1.getScheduleLink().equals(course2.getScheduleLink());
    }

    protected void mergingFunction(Course course1, Course course2) {
        course1.getProfessors().addAll(course2.getProfessors());
        course1.getScheduleClasses().addAll(course2.getScheduleClasses());
    }
}
