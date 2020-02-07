package com.lonn.scheduleparser.parsing.parsers;

import com.lonn.scheduleparser.parsing.abstractions.Parser;
import com.lonn.scheduleparser.parsing.mappers.ProfessorMapper;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Professor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProfessorParser extends Parser<Professor> {
	public ProfessorParser() {
		this.mapper = new ProfessorMapper();
	}

	protected Elements getListOfParsableElements(Document document) {
		return document.select("table")
				.get(0)
				.select("a");
	}

	protected Professor parseSingleEntity(Element parsableElement) {
		return mapper.map(parsableElement);
	}
}
