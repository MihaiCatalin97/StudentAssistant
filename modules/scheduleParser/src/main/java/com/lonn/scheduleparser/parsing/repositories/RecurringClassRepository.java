package com.lonn.scheduleparser.parsing.repositories;

import com.lonn.scheduleparser.parsing.abstractions.Repository;
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
