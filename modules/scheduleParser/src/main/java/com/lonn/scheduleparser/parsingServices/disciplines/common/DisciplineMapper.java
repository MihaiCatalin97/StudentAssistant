package com.lonn.scheduleparser.parsingServices.disciplines.common;

import com.lonn.scheduleparser.parsingServices.abstractions.Mapper;
import com.lonn.scheduleparser.parsingServices.tableParseResults.DisciplinesMainPageParseResult;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;

import org.jsoup.nodes.Element;

import static com.lonn.scheduleparser.parsingServices.ScheduleConstants.CURRENT_SEMESTER;
import static com.lonn.scheduleparser.parsingServices.tableModels.DisciplinesMainPageTableModel.COURSE_DESCRIPTION;
import static com.lonn.scheduleparser.parsingServices.tableModels.DisciplinesMainPageTableModel.COURSE_NAME;
import static com.lonn.scheduleparser.parsingServices.tableParseResults.DisciplinesMainPageParseResult.fromRow;

public abstract class DisciplineMapper<T extends Discipline> extends Mapper<T> {
	public T map(Element tableRow) {
		if (shouldParseRow(tableRow)) {
			Discipline parsedDiscipline = map(fromRow(tableRow));

			return (T) parsedDiscipline;
		}

		return null;
	}

	public Discipline map(DisciplinesMainPageParseResult parseResult) {
		String description = parseResult.getTextOfColumn(COURSE_DESCRIPTION);
		String name = parseResult.getTextOfColumn(COURSE_NAME);

		return newEntityInstance()
				.setDisciplineName(name)
				.setDescription(generateDisciplineDescriptionForName(name))
				.setScheduleLink(parseResult.getLinkOfColumn(COURSE_NAME))
				.setYear(getDisciplineYearFromDescription(description))
				.setSemester(CURRENT_SEMESTER);
	}

	protected abstract Boolean shouldParseRow(Element tableRow);

	protected abstract T newEntityInstance();

	private Integer getDisciplineYearFromDescription(String description) {
		for (int an = 1; an <= 3; an++) {
			if (description.toLowerCase()
					.contains("anul " + an)) {
				return an;
			}
		}

		return 0;
	}

	private String generateDisciplineDescriptionForName(String disciplineName) {
		return "Description for the discipline \"" + disciplineName + "\"";
	}
}
