package com.lonn.scheduleparser.parsers;

import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

import org.jsoup.nodes.Document;

import java.util.List;

public interface Parser<T extends BaseEntity> {
    List<T> parse(Document doc);
}
