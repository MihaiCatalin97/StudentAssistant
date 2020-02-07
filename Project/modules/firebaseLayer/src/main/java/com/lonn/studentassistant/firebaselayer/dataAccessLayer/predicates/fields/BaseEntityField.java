package com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;

public class BaseEntityField<T extends BaseEntity, X> extends EntityField<T, X> {
	public static BaseEntityField<BaseEntity, String> ID = new BaseEntityField<>("id");

	protected BaseEntityField(String fieldName) {
		this.fieldName = fieldName;
	}
}
