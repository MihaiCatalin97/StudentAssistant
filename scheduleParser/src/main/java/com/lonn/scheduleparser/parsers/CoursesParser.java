package com.lonn.scheduleparser.parsers;

import com.lonn.scheduleparser.mappers.HtmlCourseMapper;
import com.lonn.scheduleparser.mergers.CourseMerger;
import com.lonn.studentassistant.firebaselayer.models.Course;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class CoursesParser implements Parser<Course> {
    private static HtmlCourseMapper courseMapper;
    private static CourseMerger courseMerger;

    static {
        courseMapper = new HtmlCourseMapper();
        courseMerger = new CourseMerger();
    }

    @Override
    public List<Course> parse(Document doc) {
        List<Course> parsedCourses = new LinkedList<>();

        Elements htmlTableRows = doc.select("table").get(0)
                .select("tr");

        for (int i = 1; i < htmlTableRows.size(); i++) {
            Element tableRow = htmlTableRows.get(i);

            if (shouldBeParsedToCourse(tableRow)) {
                parsedCourses.add(courseMapper.map(tableRow));
            }
        }

        return courseMerger.merge(parsedCourses);
    }

    private boolean shouldBeParsedToCourse(Element tableRow) {
        String type = getActivityTypeFromRow(tableRow);

        return type != null &&
                (type.equals("Curs") ||
                        type.equals("Laborator") ||
                        type.equals("Serminar"));
    }

    private String getActivityTypeFromRow(Element tableRow) {
        Elements tableDivs = tableRow.select("td");

        if (tableDivs.size() != 8) {
            return null;
        }
        return tableRow.select("td")
                .get(3)
                .text();
    }
}
