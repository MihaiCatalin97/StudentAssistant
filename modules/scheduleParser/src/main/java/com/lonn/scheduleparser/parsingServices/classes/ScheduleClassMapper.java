package com.lonn.scheduleparser.parsingServices.classes;

import com.lonn.scheduleparser.parsingServices.abstractions.Mapper;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class ScheduleClassMapper extends Mapper<ScheduleClass> {
    public ScheduleClass map(Element scheduleClassRow) {
        if (isParsableRow(scheduleClassRow)) {
            return new ScheduleClass()
                    .setStartHour(getStartHourFromRow(scheduleClassRow))
                    .setEndHour(getEndHourFromRow(scheduleClassRow))
                    .setType(getClassTypeFromRow(scheduleClassRow))
                    .setGroups(getGroupsFromRow(scheduleClassRow))
                    .setProfessors(getProfessorScheduleLinksFromRow(scheduleClassRow))
                    .setRooms(getRoomsFromRow(scheduleClassRow))
                    .setParity(getParityFromRow(scheduleClassRow));
        }
        return null;
    }

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

    private Set<String> getGroupsFromRow(Element tableRow) {
        return new HashSet<>(asList(tableRow.select("td")
                .get(4)
                .text()
                .replace(" ", "")
                .split(",")));
    }

    private Set<String> getProfessorScheduleLinksFromRow(Element tableRow) {
        Elements professorAnchors = tableRow.select("td")
                .get(5)
                .select("a");

        Set<String> result = new HashSet<>();

        for (Element professorAnchor : professorAnchors) {
            result.add(professorAnchor
                    .attr("abs:href"));
        }

        return result;
    }

    private Set<String> getRoomsFromRow(Element tableRow) {
        return new HashSet<>(asList(tableRow.select("td")
                .get(6)
                .text()
                .replace(" ", "")
                .split(",")));
    }

    private String getParityFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(7)
                .text();
    }

    private Boolean isParsableRow(Element tableRow) {
        return tableRow.select("td")
                .size() == 9;
    }
}
