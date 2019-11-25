package com.lonn.scheduleparser.parsingServices.classes.recurring;

import com.lonn.scheduleparser.parsingServices.classes.common.ScheduleClassParsingService;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;

public class RecurringClassParsingService extends ScheduleClassParsingService<RecurringClass> {
    private static RecurringClassParsingService instance;

    private RecurringClassParsingService() {
        repository = RecurringClassRepository.getInstance();
        parser = new RecurringClassParser();
    }

    public static RecurringClassParsingService getInstance() {
        if (instance == null) {
            instance = new RecurringClassParsingService();
        }
        return instance;
    }
}
