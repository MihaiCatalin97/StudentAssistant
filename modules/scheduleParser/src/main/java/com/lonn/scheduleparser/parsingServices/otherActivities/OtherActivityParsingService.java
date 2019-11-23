package com.lonn.scheduleparser.parsingServices.otherActivities;

import com.lonn.scheduleparser.parsingServices.abstractions.ParsingService;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;

import java.util.List;
import java.util.concurrent.Future;

import static com.lonn.scheduleparser.parsingServices.ScheduleConstants.COURSES_PAGE;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class OtherActivityParsingService extends ParsingService<OtherActivity> {
    private static OtherActivityParsingService instance;

    private OtherActivityParsingService() {
        repository = OtherActivityRepository.getInstance();
        parser = new OtherActivityParser();
    }

    public static OtherActivityParsingService getInstance() {
        if (instance == null) {
            instance = new OtherActivityParsingService();
        }
        return instance;
    }

    protected Future<List<OtherActivity>> parse() {
        return newSingleThreadExecutor().submit(() -> {
            repository.addAll(parseSinglePage(COURSES_PAGE));
            return repository.getAll();
        });
    }
}