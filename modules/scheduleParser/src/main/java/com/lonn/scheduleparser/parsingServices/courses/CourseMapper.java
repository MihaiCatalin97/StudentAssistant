package com.lonn.scheduleparser.parsingServices.courses;

import com.lonn.scheduleparser.parsingServices.abstractions.Mapper;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.CycleSpecializations;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.lonn.scheduleparser.parsingServices.ScheduleConstants.CURRENT_SEMESTER;

public class CourseMapper extends Mapper<Course> {
    public Course map(Element courseTableRow) {
        CycleSpecializations cycleAndSpecialization =
                getCourseCycleAndSpecialization(courseTableRow);

        if (cycleAndSpecialization != null) {
            String courseName = getCourseNameFromRow(courseTableRow);

            return new Course()
                    .setCourseName(courseName)
                    .setDescription(generateCourseDescriptionForName(courseName))
                    .setCycleAndSpecialization(cycleAndSpecialization)
                    .setYear(getCourseYearFromRow(courseTableRow))
                    .setScheduleLink(getCourseScheduleLinkFromRow(courseTableRow))
                    .setSemester(CURRENT_SEMESTER);
        }

        return null;
    }

    private String getCourseNameFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(0)
                .select("a")
                .text();
    }

    private String getCourseScheduleLinkFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(0)
                .select("a")
                .get(0)
                .attr("abs:href");
    }

    private Integer getCourseYearFromRow(Element tableRow) {
        String courseDescription = tableRow.select("td")
                .get(1)
                .text();

        for (int an = 1; an <= 3; an++) {
            if (courseDescription.toLowerCase()
                    .contains("anul " + an)) {
                return an;
            }
        }

        return 0;
    }

    private CycleSpecializations getCourseCycleAndSpecialization(Element tableRow) {
        Elements tableDivs = tableRow.select("td");
        String courseDescription = tableDivs.get(1)
                .text();

        for (CycleSpecializations cycleSpecialization : CycleSpecializations.values()) {
            if (courseDescription.toLowerCase()
                    .contains(cycleSpecialization.toString()
                            .toLowerCase())) {
                return cycleSpecialization;
            }
        }

        return null;
    }

    private String generateCourseDescriptionForName(String courseName) {
        return "Description for the course \"" + courseName + "\"";
    }
}
