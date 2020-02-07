package com.lonn.scheduleparser.parsing.mappers;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OtherActivity;

import org.jsoup.nodes.Element;

public class OtherActivityMapper extends DisciplineMapper<OtherActivity> {
	@Override
	protected Boolean shouldParseRow(Element tableRow) {
		return !tableRow.select("td")
				.get(1)
				.text()
				.contains("anul");
	}

	@Override
	protected OtherActivity newEntityInstance() {
		return new OtherActivity();
	}
}
