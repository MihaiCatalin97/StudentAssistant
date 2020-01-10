package com.lonn.scheduleparser.parsing.parsers;

import com.lonn.scheduleparser.parsing.abstractions.Parser;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public abstract class DisciplineParser<T extends Discipline> extends Parser<T> {
	protected Elements getListOfParsableElements(Document document) {
		return document.select("table")
				.get(0)
				.select("tr");
	}
}
