package com.lonn.scheduleparser.parsingServices.otherActivities;

import com.lonn.scheduleparser.parsingServices.abstractions.Parser;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class OtherActivityParser extends Parser<OtherActivity> {
    public OtherActivityParser(){
        this.mapper = new OtherActivityMapper();
    }

    protected Elements getListOfParsableElements(Document document) {
        return document.select("table")
                .get(0)
                .select("tr");
    }

    protected OtherActivity parseSingleEntity(Element parsableElement) {
        return mapper.map(parsableElement);
    }
}
