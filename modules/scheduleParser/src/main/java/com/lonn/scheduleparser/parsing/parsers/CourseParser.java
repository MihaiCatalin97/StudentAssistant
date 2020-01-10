package com.lonn.scheduleparser.parsing.parsers;

import com.lonn.scheduleparser.parsing.DocumentHolder;
import com.lonn.scheduleparser.parsing.mappers.CourseMapper;
import com.lonn.studentassistant.firebaselayer.entities.Course;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CourseParser extends DisciplineParser<Course> {
	public CourseParser() {
		this.mapper = new CourseMapper();
	}

	protected Course parseSingleEntity(Element parsableElement) {
		Course parsedEntity = mapper.map(parsableElement);

		if (parsedEntity != null) {
			parsedEntity.setPack(getOptionalPack(parsedEntity));
		}

		return parsedEntity;
	}

	private int getOptionalPack(Course course) {
		try {
			Document document = DocumentHolder.getDocumentForLink(course.getScheduleLink());
			return Integer.parseInt(document.select("table")
					.get(0)
					.select("tr")
					.get(2)
					.select("td")
					.get(8)
					.text());
		}
		catch (Exception exception) {
			return 0;
		}
	}
}
