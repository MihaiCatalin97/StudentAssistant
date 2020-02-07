package com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public abstract class EntityField<T extends BaseEntity, X> {
	@Getter
	protected String fieldName;
}
