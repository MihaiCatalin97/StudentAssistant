package com.lonn.scheduleparser.parsingServices.classes;

import com.lonn.scheduleparser.parsingServices.abstractions.Repository;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;

public class ScheduleClassRepository extends Repository<ScheduleClass> {
    private static ScheduleClassRepository instance;

    private ScheduleClassRepository() {
    }

    public static ScheduleClassRepository getInstance() {
        if (instance == null) {
            instance = new ScheduleClassRepository();
        }
        return instance;
    }

    public ScheduleClass findByScheduleLink(String scheduleLink) {
        return null;
    }
}
