package com.lonn.scheduleparser.parsing.services;

import com.lonn.scheduleparser.parsing.parsers.OtherActivityParser;
import com.lonn.scheduleparser.parsing.repositories.OtherActivityRepository;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;

public class OtherActivityParsingService extends DisciplineParsingService<OtherActivity> {
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
}