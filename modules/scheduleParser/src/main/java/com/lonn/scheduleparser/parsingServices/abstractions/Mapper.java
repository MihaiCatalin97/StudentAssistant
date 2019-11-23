package com.lonn.scheduleparser.parsingServices.abstractions;

import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

import org.jsoup.nodes.Element;

public abstract class Mapper<T extends BaseEntity> {
    public abstract T map(Element parsableElement);
}
