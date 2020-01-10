package com.lonn.scheduleparser.parsing.tableModels;

import android.util.SparseArray;

public class DisciplineScheduleTableModel {
	public static final TableColumn FROM_HOUR;
	public static final TableColumn TO_HOUR;
	public static final TableColumn SUBJECT;
	public static final TableColumn TYPE;
	public static final TableColumn STUDENT_GROUPS;
	public static final TableColumn PROFESSORS;
	public static final TableColumn ROOMS;
	public static final TableColumn FREQUENCY;
	public static final TableColumn PACKAGE;
	public static final SparseArray<TableColumn> COLUMN_INDEX_MAP;

	static {
		COLUMN_INDEX_MAP = new SparseArray<>();

		FROM_HOUR = createColumn(0);
		TO_HOUR = createColumn(1);
		SUBJECT = createColumn(2);
		TYPE = createColumn(3);
		STUDENT_GROUPS = createColumn(4);
		PROFESSORS = createColumn(5);
		ROOMS = createColumn(6);
		FREQUENCY = createColumn(7);
		PACKAGE = createColumn(8);
	}

	private DisciplineScheduleTableModel() {
	}

	private static TableColumn createColumn(Integer columnIndex) {
		TableColumn result = new TableColumn(columnIndex);
		COLUMN_INDEX_MAP.put(columnIndex, result);
		return result;
	}

	public static TableColumn getColumnByIndex(Integer columnIndex) {
		return COLUMN_INDEX_MAP.get(columnIndex);
	}
}
