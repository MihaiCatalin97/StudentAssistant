package com.lonn.scheduleparser.parsingServices.disciplines.otherActivities;

import com.lonn.scheduleparser.parsingServices.disciplines.common.DisciplineParsingService;
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