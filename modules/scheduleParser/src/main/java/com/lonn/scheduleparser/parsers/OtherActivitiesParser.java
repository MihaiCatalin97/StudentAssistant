package com.lonn.scheduleparser.parsers;

import com.lonn.scheduleparser.mappers.HtmlOtherActivityMapper;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class OtherActivitiesParser implements Parser<OtherActivity> {
    private static HtmlOtherActivityMapper otherActivityMapper;

    static {
        otherActivityMapper = new HtmlOtherActivityMapper();
    }

    @Override
    public List<OtherActivity> parse(Document doc) {
        List<OtherActivity> parsedActivities = new LinkedList<>();

        Elements htmlTableRows = doc.select("table").get(0)
                .select("tr");

        for (int i = 1; i < htmlTableRows.size(); i++) {
            Element tableRow = htmlTableRows.get(i);

            if (shouldBeParsedToActivity(tableRow)) {
                parsedActivities.add(otherActivityMapper.map(tableRow));
            }
        }

        return parsedActivities;
    }

    private boolean shouldBeParsedToActivity(Element tableRow) {
        String type = getActivityTypeFromRow(tableRow);

        return type != null &&
                !(type.equals("Curs") ||
                        type.equals("Laborator") ||
                        type.equals("Seminar"));
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
