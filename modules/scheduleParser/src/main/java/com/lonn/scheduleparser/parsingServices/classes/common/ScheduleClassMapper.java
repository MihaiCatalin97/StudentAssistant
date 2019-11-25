package com.lonn.scheduleparser.parsingServices.classes.common;

import com.lonn.scheduleparser.parsingServices.abstractions.Mapper;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

public abstract class ScheduleClassMapper<T extends ScheduleClass> extends Mapper<T> {
    public T map(Element tableRow) {
        if (isParsableRow(tableRow)) {
            ScheduleClass result = newMappedEntity().setStartHour(getStartHourFromRow(tableRow))
                    .setEndHour(getEndHourFromRow(tableRow))
                    .setType(getClassTypeFromRow(tableRow))
                    .setGroups(getGroupsFromRow(tableRow))
                    .setProfessors(getProfessorScheduleLinksFromRow(tableRow))
                    .setRooms(getRoomsFromRow(tableRow));

            return (T) result;
        }

        return null;
    }

    protected abstract Boolean isParsableRow(Element tableRow);

    protected abstract T newMappedEntity();

    private Integer getStartHourFromRow(Element tableRow) {
        return Integer.parseInt(tableRow.select("td")
                .get(0)
                .text()
                .replace(":", ""));
    }

    private Integer getEndHourFromRow(Element tableRow) {
        return Integer.parseInt(tableRow.select("td")
                .get(1)
                .text()
                .replace(":", ""));
    }

    private String getClassTypeFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(3)
                .text();
    }

    private List<String> getGroupsFromRow(Element tableRow) {
        return asList(tableRow.select("td")
                .get(4)
                .text()
                .replace(" ", "")
                .split(","));
    }

    private List<String> getProfessorScheduleLinksFromRow(Element tableRow) {
        Elements professorAnchors = tableRow.select("td")
                .get(5)
                .select("a");

        List<String> result = new LinkedList<>();

        for (Element professorAnchor : professorAnchors) {
            result.add(professorAnchor
                    .attr("abs:href"));
        }

        return result;
    }

    private List<String> getRoomsFromRow(Element tableRow) {
        return asList(tableRow.select("td")
                .get(6)
                .text()
                .replace(" ", "")
                .split(","));
    }
}
