package com.lonn.scheduleparser.parsingServices.classes.oneTime;

import com.lonn.scheduleparser.parsingServices.abstractions.Repository;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;

public class OneTimeClassRepository extends Repository<OneTimeClass> {
    private static OneTimeClassRepository instance;

    private OneTimeClassRepository() {
    }

    public static OneTimeClassRepository getInstance() {
        if (instance == null) {
            instance = new OneTimeClassRepository();
        }
        return instance;
    }

    public OneTimeClass findByScheduleLink(String scheduleLink) {
        return null;
    }
}
