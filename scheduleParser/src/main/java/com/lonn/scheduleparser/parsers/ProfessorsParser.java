package com.lonn.scheduleparser.parsers;

import com.lonn.scheduleparser.mappers.HtmlProfessorMapper;
import com.lonn.studentassistant.firebaselayer.models.Professor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

class ProfessorsParser {
    private static final HtmlProfessorMapper professorMapper = new HtmlProfessorMapper();

    List<Professor> parse(Document doc) {
        List<Professor> parsedProfessors = new LinkedList<>();

        Elements htmlProfessorListItems = doc.select("li");

        for (Element htmlProfessorListItem : htmlProfessorListItems) {
            Element anchorProfessorItem = htmlProfessorListItem.selectFirst("a");
            parsedProfessors.add(professorMapper.map(anchorProfessorItem));
        }

        return parsedProfessors;
    }
}
