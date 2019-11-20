package com.lonn.scheduleparser.repositories;

import com.lonn.scheduleparser.mergers.ScheduleClassMerger;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;

public class ScheduleClassRepository extends Repository<ScheduleClass> {
    private static ScheduleClassRepository instance;

    private ScheduleClassRepository() {
        merger = new ScheduleClassMerger();
    }

    public static ScheduleClassRepository getInstance() {
        if (instance == null) {
            instance = new ScheduleClassRepository();
        }
        return instance;
    }
}
