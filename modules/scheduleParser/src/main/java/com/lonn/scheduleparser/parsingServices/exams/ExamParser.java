package com.lonn.scheduleparser.parsingServices.exams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lonn.scheduleparser.parsingServices.abstractions.Parser;
import com.lonn.studentassistant.firebaselayer.models.Exam;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class ExamParser extends Parser<Exam> {
    private static Map<String, Integer> dayMap;

    static {
        dayMap = new HashMap<>();

        dayMap.put("Luni", 1);
        dayMap.put("Marti", 2);
        dayMap.put("Miercuri", 3);
        dayMap.put("Joi", 4);
        dayMap.put("Vineri", 5);
        dayMap.put("Sambata", 6);
        dayMap.put("Duminica", 7);
    }

    private Integer dayNumber = null;

    public ExamParser() {
        this.mapper = new ExamMapper();
    }

    protected Elements getListOfParsableElements(Document document) {
        try {
            Elements rowsToBeParsed = document.select("table")
                    .get(1)
                    .select("tr");

            if (rowsToBeParsed.get(0)
                    .select("td")
                    .size() == 8) {
                return rowsToBeParsed;
            }

            return null;
        } catch (Exception exception) {
            return null;
        }
    }

    protected Exam parseSingleEntity(Element parsableElement) {
        Exam parsedEntity = mapper.map(parsableElement);

        if (parsedEntity == null) {
            Integer parsedDayNumber = getDayFromMarkingRow(parsableElement);

            if (parsedDayNumber != null) {
                dayNumber = parsedDayNumber;
            }
        }
        else {
            parsedEntity.setDay(dayNumber);
        }

        return parsedEntity;
    }

    @Nullable
    private Integer getDayFromMarkingRow(@NonNull Element tableRowElement) {
        try {
            String dayName = tableRowElement.text()
                    .split(" ")[0];
            return dayMap.get(dayName);
        } catch (IndexOutOfBoundsException exception) {
            return null;
        }
    }
}
