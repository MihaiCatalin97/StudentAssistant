package com.lonn.scheduleparser.parsingServices.courses;

import com.lonn.scheduleparser.parsingServices.abstractions.Parser;
import com.lonn.studentassistant.firebaselayer.models.Course;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class CourseParser extends Parser<Course> {
    public CourseParser() {
        this.mapper = new CourseMapper();
    }

    protected Elements getListOfParsableElements(Document document) {
        return document.select("table")
                .get(0)
                .select("tr");
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
