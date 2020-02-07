package com.lonn.studentassistant.firebaselayer.dataAccessLayer.database;

import com.lonn.studentassistant.firebaselayer.Utils;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;

import lombok.Getter;

@Getter
public class DatabaseTable<T extends BaseEntity> {
	private Class<T> tableClass;
	private String tableName;

	DatabaseTable(Class<T> tableClass, String tableName) {
		this.tableClass = tableClass;
		this.tableName = Utils.stringToFirstCapital(tableName);
	}
}
