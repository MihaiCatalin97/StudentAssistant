package com.lonn.scheduleparser.parsing.mappers;

import com.lonn.scheduleparser.parsing.abstractions.Mapper;
import com.lonn.scheduleparser.parsing.tableParseResults.DisciplinesMainPageParseResult;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;

import org.jsoup.nodes.Element;

import static com.lonn.scheduleparser.parsing.ScheduleConstants.CURRENT_SEMESTER;
import static com.lonn.scheduleparser.parsing.tableModels.DisciplinesMainPageTableModel.DISCIPLINE_NAME;
import static com.lonn.scheduleparser.parsing.tableParseResults.DisciplinesMainPageParseResult.fromRow;

public abstract class DisciplineMapper<T extends Discipline> extends Mapper<T> {
	public T map(Element tableRow) {
		if (shouldParseRow(tableRow)) {
			Discipline parsedDiscipline = map(fromRow(tableRow));

			return (T) parsedDiscipline;
		}

		return null;
	}

	public Discipline map(DisciplinesMainPageParseResult parseResult) {
		String name = parseResult.getTextOfColumn(DISCIPLINE_NAME);

		return newEntityInstance()
				.setDisciplineName(name)
				.setDescription(generateDisciplineDescriptionForName(name))
				.setScheduleLink(parseResult.getLinkOfColumn(DISCIPLINE_NAME))
				.setWebsite(parseResult.getLinkOfColumn(DISCIPLINE_NAME))
				.setSemester(CURRENT_SEMESTER);
	}

	protected abstract Boolean shouldParseRow(Element tableRow);

	protected abstract T newEntityInstance();


	private String generateDisciplineDescriptionForName(String disciplineName) {
		return "Description for the discipline \"" + disciplineName + "\"";
	}
}
