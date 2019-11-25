package com.lonn.scheduleparser.parsingServices.classes.oneTime;

import com.lonn.scheduleparser.parsingServices.classes.common.ScheduleClassMapper;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;

import org.jsoup.nodes.Element;

public class OneTimeClassMapper extends ScheduleClassMapper<OneTimeClass> {
    @Override
    protected Boolean isParsableRow(Element tableRow) {
        return tableRow.select("td")
                .size() == 8;
    }

    @Override
    protected OneTimeClass newMappedEntity(){
        return new OneTimeClass();
    }
}
