package com.lonn.scheduleparser.parsers;

import com.lonn.scheduleparser.mappers.HtmlScheduleClassMapper;
import com.lonn.scheduleparser.repositories.CourseRepository;
import com.lonn.scheduleparser.repositories.OtherActivityRepository;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ScheduleClassesParser implements Parser<ScheduleClass> {
    private static Map<String, Integer> dayMap;
    private static HtmlScheduleClassMapper scheduleClassMapper;
    private static CourseRepository courseRepository;
    private static OtherActivityRepository otherActivityRepository;

    static {
        dayMap = new HashMap<>();

        dayMap.put("Luni", 1);
        dayMap.put("Marti", 2);
        dayMap.put("Miercuri", 3);
        dayMap.put("Joi", 4);
        dayMap.put("Vineri", 5);
        dayMap.put("Sambata", 6);
        dayMap.put("Duminica", 7);

        scheduleClassMapper = new HtmlScheduleClassMapper();
        courseRepository = CourseRepository.getInstance();
        otherActivityRepository = OtherActivityRepository.getInstance();
    }

    @Override
    public List<ScheduleClass> parse(Document doc) {
        List<ScheduleClass> parsedScheduleClasses = new LinkedList<>();

        Elements htmlTableRows = doc.select("table").get(0)
                .select("tr");

        Integer dayNumber = null;

        for (Element tableRow : htmlTableRows) {
            if (isDayMarkingRow(tableRow)) {
                dayNumber = getDayFromMarkingRow(tableRow);
            }
            else if (dayNumber != null) {
                String activityScheduleLink = getActivityScheduleLinkFromRow(tableRow);

                Course parentCourse = courseRepository.findByScheduleLink(activityScheduleLink);
                OtherActivity parentActivity = otherActivityRepository.findByScheduleLink(activityScheduleLink);

                String parentActivityKey = null;

                if (parentCourse != null) {
                    parentActivityKey = parentCourse.getKey();
                }
                else if (parentActivity != null) {
                    parentActivityKey = parentActivity.getKey();
                }

                ScheduleClass parsedScheduleClass = scheduleClassMapper.map(tableRow)
                        .setDay(dayNumber)
                        .setCourse(parentActivityKey);

                parsedScheduleClasses.add(parsedScheduleClass);
            }
        }

        return parsedScheduleClasses;
    }

    private String getActivityScheduleLinkFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(2)
                .select("a")
                .get(0)
                .attr("abs:href");
    }

    private boolean isDayMarkingRow(Element tableRowElement) {
        return tableRowElement.select("td").size() == 1;
    }

    private Integer getDayFromMarkingRow(Element tableRowElement) {
        String dayName = tableRowElement.text()
                .split(" ")[0];
        return dayMap.get(dayName);
    }
}
