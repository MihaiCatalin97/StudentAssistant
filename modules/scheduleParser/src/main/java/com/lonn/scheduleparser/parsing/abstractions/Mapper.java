package com.lonn.scheduleparser.parsing.abstractions;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;

import org.jsoup.nodes.Element;

public abstract class Mapper<T extends BaseEntity> {
	public abstract T map(Element parsableElement);
}
