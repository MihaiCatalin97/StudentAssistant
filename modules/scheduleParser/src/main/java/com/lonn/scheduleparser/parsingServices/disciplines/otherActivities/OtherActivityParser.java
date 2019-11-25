package com.lonn.scheduleparser.parsingServices.disciplines.otherActivities;

import com.lonn.scheduleparser.parsingServices.abstractions.Parser;
import com.lonn.scheduleparser.parsingServices.disciplines.common.DisciplineParser;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OtherActivityParser extends DisciplineParser<OtherActivity> {
    public OtherActivityParser(){
        this.mapper = new OtherActivityMapper();
    }

    protected OtherActivity parseSingleEntity(Element parsableElement) {
        return mapper.map(parsableElement);
    }
}
