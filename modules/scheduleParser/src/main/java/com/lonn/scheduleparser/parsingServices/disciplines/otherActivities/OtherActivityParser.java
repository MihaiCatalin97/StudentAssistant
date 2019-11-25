package com.lonn.scheduleparser.parsingServices.disciplines.otherActivities;

import com.lonn.scheduleparser.parsingServices.DocumentHolder;
import com.lonn.scheduleparser.parsingServices.disciplines.common.DisciplineParser;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class OtherActivityParser extends DisciplineParser<OtherActivity> {
	public OtherActivityParser() {
		this.mapper = new OtherActivityMapper();
	}

	protected OtherActivity parseSingleEntity(Element parsableElement) {
		OtherActivity parsedEntity = mapper.map(parsableElement);

		if (parsedEntity != null) {
			parsedEntity.setType(getOtherActivityType(parsedEntity));
		}

		return parsedEntity;
	}

	private String getOtherActivityType(OtherActivity otherActivity) {
		try {
			Document document = DocumentHolder.getDocumentForLink(otherActivity.getScheduleLink());
			return document.select("table")
					.get(0)
					.select("tr")
					.get(2)
					.select("td")
					.get(5)
					.text();
		}
		catch (Exception exception) {
			return null;
		}
	}
}
