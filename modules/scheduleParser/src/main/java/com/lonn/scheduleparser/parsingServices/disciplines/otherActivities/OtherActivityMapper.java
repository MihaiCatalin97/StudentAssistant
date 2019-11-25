package com.lonn.scheduleparser.parsingServices.disciplines.otherActivities;

import com.lonn.scheduleparser.parsingServices.disciplines.common.DisciplineMapper;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;

import org.jsoup.nodes.Element;

public class OtherActivityMapper extends DisciplineMapper<OtherActivity> {
    @Override
    protected Boolean shouldParseRow(Element tableRow) {
        return !tableRow.select("td")
                .get(1)
                .text()
                .contains("anul");
    }

    @Override
    protected OtherActivity newEntityInstance() {
        return new OtherActivity();
    }
}
