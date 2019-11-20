package com.lonn.scheduleparser.mappers;

import com.lonn.studentassistant.firebaselayer.models.Course;

import org.jsoup.nodes.Element;

public class HtmlCourseMapper {
    public Course map(Element courseTableRow) {
        String courseName = getCourseNameFromRow(courseTableRow);

        return new Course()
                .setCourseName(courseName)
                .setDescription(generateCourseDescriptionForName(courseName))
                .setPack(getCoursePackFromRow(courseTableRow))
                .setYear(getCourseYearFromRow(courseTableRow))
                .setScheduleLink(getCourseScheduleLinkFromRow(courseTableRow))
                .setSemester(2);
    }

    private String getCourseNameFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(2)
                .text();
    }

    private String getCourseScheduleLinkFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(2)
                .select("a")
                .get(0)
                .attr("abs:href");
    }

    private Integer getCourseYearFromRow(Element tableRow) {
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

    private Integer getCoursePackFromRow(Element tableRow) {
        try {
            return Integer.parseInt(tableRow.select("td")
                    .get(7)
                    .text());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String generateCourseDescriptionForName(String courseName) {
        return "Description for the course \"" + courseName + "\"";
    }
}
