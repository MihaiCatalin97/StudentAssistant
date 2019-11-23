package com.lonn.scheduleparser.parsingServices.otherActivities;

import com.lonn.scheduleparser.parsingServices.abstractions.Mapper;
import com.lonn.studentassistant.firebaselayer.models.CycleSpecializations;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.lonn.scheduleparser.parsingServices.ScheduleConstants.CURRENT_SEMESTER;

public class OtherActivityMapper extends Mapper<OtherActivity> {
    public OtherActivity map(Element otherActivityTableRow) {
        if (isOtherActivity(otherActivityTableRow)) {
            String otherActivityName = getOtherActivityNameFromRow(otherActivityTableRow);

            return new OtherActivity()
                    .setActivityName(otherActivityName)
                    .setDescription(generateOtherActivityDescriptionForName(otherActivityName))
                    .setYear(getOtherActivityYearFromRow(otherActivityTableRow))
                    .setScheduleLink(getOtherActivityScheduleLinkFromRow(otherActivityTableRow))
                    .setSemester(CURRENT_SEMESTER);
        }

        return null;
    }

    private String getOtherActivityNameFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(0)
                .select("a")
                .text();
    }

    private String getOtherActivityScheduleLinkFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(0)
                .select("a")
                .get(0)
                .attr("abs:href");
    }

    private Integer getOtherActivityYearFromRow(Element tableRow) {
        String otherActivityDescription = tableRow.select("td")
                .get(1)
                .text();

        for (int an = 1; an <= 3; an++) {
            if (otherActivityDescription.toLowerCase()
                    .contains("anul " + an)) {
                return an;
            }
        }

        return 0;
    }

    private boolean isOtherActivity(Element tableRow) {
        Elements tableDivs = tableRow.select("td");
        String otherActivityDescription = tableDivs.get(1)
                .text();

        for (CycleSpecializations cycleSpecialization : CycleSpecializations.values()) {
            if (otherActivityDescription.toLowerCase()
                    .contains(cycleSpecialization.toString()
                            .toLowerCase())) {
                return false;
            }
        }

        return true;
    }

    private String generateOtherActivityDescriptionForName(String otherActivityName) {
        return "Description for the otherActivity \"" + otherActivityName + "\"";
    }
}
