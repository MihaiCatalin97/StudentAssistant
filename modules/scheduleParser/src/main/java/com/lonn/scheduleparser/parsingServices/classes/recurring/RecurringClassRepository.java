package com.lonn.scheduleparser.parsingServices.classes.recurring;

import com.lonn.scheduleparser.parsingServices.abstractions.Repository;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;

public class RecurringClassRepository extends Repository<RecurringClass> {
    private static RecurringClassRepository instance;

    private RecurringClassRepository() {
    }

    public static RecurringClassRepository getInstance() {
        if (instance == null) {
            instance = new RecurringClassRepository();
        }
        return instance;
    }

    public RecurringClass findByScheduleLink(String scheduleLink) {
        return null;
    }
}
