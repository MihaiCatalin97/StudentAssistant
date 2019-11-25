package com.lonn.scheduleparser.parsingServices.professors;

import com.lonn.scheduleparser.parsingServices.abstractions.Parser;
import com.lonn.studentassistant.firebaselayer.entities.Professor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProfessorParser extends Parser<Professor> {
    public ProfessorParser() {
        this.mapper = new ProfessorMapper();
    }

    protected Elements getListOfParsableElements(Document document) {
        return document.select("table")
                .get(0)
                .select("a");
    }

    protected Professor parseSingleEntity(Element parsableElement) {
        return mapper.map(parsableElement);
    }
}
