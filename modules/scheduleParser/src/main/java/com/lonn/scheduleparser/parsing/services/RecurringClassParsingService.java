package com.lonn.scheduleparser.parsing.services;

import com.lonn.scheduleparser.parsing.parsers.RecurringClassParser;
import com.lonn.scheduleparser.parsing.repositories.RecurringClassRepository;
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
