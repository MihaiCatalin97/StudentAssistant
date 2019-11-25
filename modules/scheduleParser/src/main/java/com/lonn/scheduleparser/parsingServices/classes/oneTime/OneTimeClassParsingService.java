package com.lonn.scheduleparser.parsingServices.classes.oneTime;

import com.lonn.scheduleparser.parsingServices.classes.common.ScheduleClassParsingService;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;

public class OneTimeClassParsingService extends ScheduleClassParsingService<OneTimeClass> {
    private static OneTimeClassParsingService instance;

    private OneTimeClassParsingService() {
        repository = OneTimeClassRepository.getInstance();
        parser = new OneTimeClassParser();
    }

    public static OneTimeClassParsingService getInstance() {
        if (instance == null) {
            instance = new OneTimeClassParsingService();
        }
        return instance;
    }
}
