package com.lonn.scheduleparser.parsing.services;

import com.lonn.scheduleparser.parsing.parsers.OneTimeClassParser;
import com.lonn.scheduleparser.parsing.repositories.OneTimeClassRepository;
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
