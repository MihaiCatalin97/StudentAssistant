package com.lonn.scheduleparser.parsing.mappers;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializations;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CourseMapper extends DisciplineMapper<Course> {
    @Override
    public Course map(Element courseTableRow) {
        Course result = super.map(courseTableRow);

        if (result != null) {
            return result.setCycleAndSpecialization(getCourseCycleAndSpecialization(courseTableRow));
        }

        return null;
    }

    @Override
    protected Boolean shouldParseRow(Element tableRow) {
        return tableRow.select("td")
                .get(1)
                .text()
                .contains("anul");
    }

    @Override
    protected Course newEntityInstance() {
        return new Course();
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
}
