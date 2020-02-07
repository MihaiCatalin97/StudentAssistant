package com.lonn.scheduleparser.parsing.repositories;

import com.lonn.scheduleparser.parsing.abstractions.Repository;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OneTimeClass;

public class OneTimeClassRepository extends Repository<OneTimeClass> {
	private static OneTimeClassRepository instance;

	private OneTimeClassRepository() {
	}

	public static OneTimeClassRepository getInstance() {
		if (instance == null) {
			instance = new OneTimeClassRepository();
		}
		return instance;
	}

	public OneTimeClass findByScheduleLink(String scheduleLink) {
		return null;
	}
}
