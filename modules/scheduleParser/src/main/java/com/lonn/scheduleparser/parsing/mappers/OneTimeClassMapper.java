package com.lonn.scheduleparser.parsing.mappers;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OneTimeClass;

import org.jsoup.nodes.Element;

public class OneTimeClassMapper extends ScheduleClassMapper<OneTimeClass> {

	@Override
	protected Boolean isParsableRow(Element tableRow) {
		return tableRow.select("td")
				.size() == 8;
	}

	@Override
	protected OneTimeClass newMappedEntity() {
		return new OneTimeClass();
	}
}
