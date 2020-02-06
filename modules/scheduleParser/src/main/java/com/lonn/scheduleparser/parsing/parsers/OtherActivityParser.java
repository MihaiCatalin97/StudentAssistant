package com.lonn.scheduleparser.parsing.parsers;

import com.lonn.scheduleparser.parsing.DocumentHolder;
import com.lonn.scheduleparser.parsing.mappers.OtherActivityMapper;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OtherActivity;

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
					.get(3)
					.text();
		}
		catch (Exception exception) {
			return null;
		}
	}
}
