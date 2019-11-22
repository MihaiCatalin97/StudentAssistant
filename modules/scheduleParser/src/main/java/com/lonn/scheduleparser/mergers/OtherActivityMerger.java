package com.lonn.scheduleparser.mergers;

import com.lonn.studentassistant.firebaselayer.models.OtherActivity;

public class OtherActivityMerger extends Merger<OtherActivity> {
    protected boolean mergingCondition(OtherActivity otherActivity1, OtherActivity otherActivity2) {
        return otherActivity1.getScheduleLink().equals(otherActivity2.getScheduleLink());
    }

    protected void mergingFunction(OtherActivity otherActivity1, OtherActivity otherActivity2) {
        otherActivity1.getProfessors().addAll(otherActivity2.getProfessors());
        otherActivity1.getScheduleClasses().addAll(otherActivity2.getScheduleClasses());
    }
}
