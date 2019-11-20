package com.lonn.scheduleparser.mappers;

import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;

import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class HtmlScheduleClassMapper {
    public ScheduleClass map(Element scheduleClassRow) {
        return new ScheduleClass()
                .setStartHour(getStartHourFromRow(scheduleClassRow))
                .setEndHour(getEndHourFromRow(scheduleClassRow))
                .setGroups(getGroupsFromRow(scheduleClassRow))
                .setRooms(getRoomsFromRow(scheduleClassRow))
                .setType(getClassTypeFromRow(scheduleClassRow));
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

    private Set<String> getGroupsFromRow(Element tableRow) {
        return new HashSet<>(asList(tableRow.select("td")
                .get(4)
                .text()
                .replace(" ", "")
                .split(",")));
    }

    private Set<String> getRoomsFromRow(Element tableRow) {
        return new HashSet<>(asList(tableRow.select("td")
                .get(5)
                .text()
                .replace(" ", "")
                .split(",")));
    }

    private String getClassTypeFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(3)
                .text();
    }
}
