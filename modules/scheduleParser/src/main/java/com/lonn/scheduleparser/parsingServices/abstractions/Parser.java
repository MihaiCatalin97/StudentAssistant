package com.lonn.scheduleparser.parsingServices.abstractions;

import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public abstract class Parser<T extends BaseEntity> {
    protected Mapper<T> mapper;

    public List<T> parse(Document document) {
        List<T> parsedEntities = new LinkedList<>();

        for (Element parsableElement : getListOfParsableElements(document)) {
            T parsedEntity = parseSingleEntity(parsableElement);

            if (parsedEntity != null) {
                parsedEntities.add(parsedEntity);
            }
        }

        return parsedEntities;
    }

    protected abstract Elements getListOfParsableElements(Document document);

    protected abstract T parseSingleEntity(Element parsableElement);
}
