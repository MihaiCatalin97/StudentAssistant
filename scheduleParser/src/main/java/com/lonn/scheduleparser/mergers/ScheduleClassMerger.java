package com.lonn.scheduleparser.mergers;

import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;

public class ScheduleClassMerger extends Merger<ScheduleClass> {
    protected boolean mergingCondition(ScheduleClass scheduleClass1, ScheduleClass scheduleClass2) {
        boolean hasCommonRooms = false;

        for (String room : scheduleClass2.getRooms()) {
            if (scheduleClass1.getRooms().contains(room)) {
                hasCommonRooms = true;
                break;
            }
        }

        return scheduleClass1.getStartHour() == scheduleClass2.getStartHour() &&
                scheduleClass1.getDay() == scheduleClass2.getDay() &&
                scheduleClass1.getCourse().equals(scheduleClass2.getCourse()) &&
                hasCommonRooms;
    }

    protected void mergingFunction(ScheduleClass scheduleClass1, ScheduleClass scheduleClass2) {
        scheduleClass1.getProfessors().addAll(scheduleClass2.getProfessors());
        scheduleClass1.getRooms().addAll(scheduleClass2.getRooms());
        scheduleClass1.getGroups().addAll(scheduleClass2.getGroups());
    }
}
