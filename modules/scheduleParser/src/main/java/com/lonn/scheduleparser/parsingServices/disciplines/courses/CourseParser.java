package com.lonn.scheduleparser.parsingServices.disciplines.courses;

import com.lonn.scheduleparser.parsingServices.abstractions.Parser;
import com.lonn.scheduleparser.parsingServices.disciplines.common.DisciplineParser;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CourseParser extends DisciplineParser<Course> {
    public CourseParser() {
        this.mapper = new CourseMapper();
    }

    protected Course parseSingleEntity(Element parsableElement) {
        Course parsedEntity = mapper.map(parsableElement);

        if (parsedEntity != null) {
            parsedEntity.setPack(getOptionalPack(parsedEntity));
        }

        return parsedEntity;
    }

    private int getOptionalPack(Course course) {
        try {
            Document document =
                    Jsoup.connect(course.getScheduleLink()).get();
            return Integer.parseInt(document.select("table")
                    .get(0)
                    .select("tr")
                    .get(2)
                    .select("td")
                    .get(8)
                    .text());
        } catch (Exception exception) {
            return 0;
        }
    }
}
