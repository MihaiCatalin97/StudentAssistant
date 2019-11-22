package com.lonn.scheduleparser.mappers;

import com.lonn.studentassistant.firebaselayer.models.OtherActivity;

import org.jsoup.nodes.Element;

public class HtmlOtherActivityMapper {
    public OtherActivity map(Element activityTableRow) {
        String activityName = getActivityNameFromRow(activityTableRow);

        return new OtherActivity()
                .setActivityName(activityName)
                .setDescription(generateActivityDescriptionForName(activityName))
                .setYear(getActivityYearFromRow(activityTableRow))
                .setType(getActivityTypeFromRow(activityTableRow))
                .setScheduleLink(getActivityScheduleLinkFromRow(activityTableRow))
                .setSemester(2);
    }

    private String getActivityNameFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(2)
                .text();
    }

    private String getActivityScheduleLinkFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(2)
                .select("a")
                .get(0)
                .attr("abs:href");
    }

    private Integer getActivityYearFromRow(Element tableRow) {
        String[] groups = tableRow.select("td")
                .get(4)
                .text()
                .split(",");

        if (groups.length == 0) {
            return 0;
        }
        if (groups[0].startsWith("I1")) {
            return 1;
        }
        if (groups[0].startsWith("I2")) {
            return 2;
        }
        if (groups[0].startsWith("I3")) {
            return 3;
        }
        return 4;
    }

    private String generateActivityDescriptionForName(String courseName) {
        return "Description for the other activity \"" + courseName + "\"";
    }

    private String getActivityTypeFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(3)
                .text();
    }
}
