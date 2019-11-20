package com.lonn.scheduleparser.mergers;

import com.lonn.studentassistant.firebaselayer.models.Professor;

public class ProfessorMerger extends Merger<Professor> {
    protected boolean mergingCondition(Professor professor1, Professor professor2) {
        return professor1.getScheduleLink().equals(professor2.getScheduleLink());
    }

    protected void mergingFunction(Professor professor1, Professor professor2) {
        professor1.getCourses().addAll(professor2.getCourses());
        professor1.getOtherActivities().addAll(professor2.getOtherActivities());
        professor1.getScheduleClasses().addAll(professor2.getScheduleClasses());
    }
}
