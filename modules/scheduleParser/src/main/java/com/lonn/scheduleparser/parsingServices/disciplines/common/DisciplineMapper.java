package com.lonn.scheduleparser.parsingServices.disciplines.common;

import com.lonn.scheduleparser.parsingServices.abstractions.Mapper;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;

import org.jsoup.nodes.Element;

import static com.lonn.scheduleparser.parsingServices.ScheduleConstants.CURRENT_SEMESTER;

public abstract class DisciplineMapper<T extends Discipline> extends Mapper<T> {
    public T map(Element tableRow) {
        if (shouldParseRow(tableRow)) {
            String otherActivityName = getDisciplineNameFromRow(tableRow);

            Discipline result = newEntityInstance()
                    .setDisciplineName(otherActivityName)
                    .setDescription(generateDisciplineDescriptionForName(otherActivityName))
                    .setYear(getDisciplineYearFromRow(tableRow))
                    .setScheduleLink(getDisciplineScheduleLinkFromRow(tableRow))
                    .setSemester(CURRENT_SEMESTER);

            return (T) result;
        }

        return null;
    }

    protected abstract Boolean shouldParseRow(Element tableRow);

    protected abstract T newEntityInstance();

    private String getDisciplineNameFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(0)
                .select("a")
                .text();
    }

    private String getDisciplineScheduleLinkFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(0)
                .select("a")
                .get(0)
                .attr("abs:href");
    }

    private Integer getDisciplineYearFromRow(Element tableRow) {
        String disciplineDescription = tableRow.select("td")
                .get(1)
                .text();

        for (int an = 1; an <= 3; an++) {
            if (disciplineDescription.toLowerCase()
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
