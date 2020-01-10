package com.lonn.scheduleparser.parsing.services;

import com.lonn.scheduleparser.parsing.abstractions.ParsingService;
import com.lonn.scheduleparser.parsing.parsers.ProfessorParser;
import com.lonn.scheduleparser.parsing.repositories.ProfessorRepository;
import com.lonn.studentassistant.firebaselayer.entities.Professor;

import java.util.List;
import java.util.concurrent.Future;

import static com.lonn.scheduleparser.parsing.ScheduleConstants.PROFESSORS_PAGE;
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
