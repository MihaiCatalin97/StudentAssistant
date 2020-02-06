package com.lonn.scheduleparser.parsing.services;

import com.lonn.scheduleparser.parsing.abstractions.ParsingService;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.Discipline;

import java.util.List;
import java.util.concurrent.Future;

import static com.lonn.scheduleparser.parsing.ScheduleConstants.COURSES_PAGE;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public abstract class DisciplineParsingService<T extends Discipline> extends ParsingService<T> {
	@Override
	protected Future<List<T>> parse() {
		return newSingleThreadExecutor().submit(() -> {
			List<T> parseResult = parseSinglePage(COURSES_PAGE);
			repository.addAll(parseResult);
			return repository.getAll();
		});
	}
}
