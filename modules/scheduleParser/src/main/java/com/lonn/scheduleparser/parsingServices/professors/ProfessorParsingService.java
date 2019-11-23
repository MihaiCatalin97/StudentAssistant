package com.lonn.scheduleparser.parsingServices.professors;

import com.lonn.scheduleparser.parsingServices.abstractions.ParsingService;
import com.lonn.studentassistant.firebaselayer.models.Professor;

import java.util.List;
import java.util.concurrent.Future;

import static com.lonn.scheduleparser.parsingServices.ScheduleConstants.PROFESSORS_PAGE;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class ProfessorParsingService extends ParsingService<Professor> {
    private static ProfessorParsingService instance;

    private ProfessorParsingService() {
        repository = ProfessorRepository.getInstance();
        parser = new ProfessorParser();
    }

    public static ProfessorParsingService getInstance() {
        if (instance == null) {
            instance = new ProfessorParsingService();
        }
        return instance;
    }

    protected Future<List<Professor>> parse() {
        return newSingleThreadExecutor().submit(() -> {
            repository.addAll(parseSinglePage(PROFESSORS_PAGE));
            return repository.getAll();
        });
    }
}
