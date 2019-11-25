package com.lonn.scheduleparser.parsingServices.disciplines.common;

import com.lonn.scheduleparser.parsingServices.abstractions.ParsingService;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;

import java.util.List;
import java.util.concurrent.Future;

import static com.lonn.scheduleparser.parsingServices.ScheduleConstants.COURSES_PAGE;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public abstract class DisciplineParsingService<T extends Discipline> extends ParsingService<T> {
    @Override
    protected Future<List<T>> parse() {
        return newSingleThreadExecutor().submit(() -> {
            repository.addAll(parseSinglePage(COURSES_PAGE));
            return repository.getAll();
        });
    }
}
