package com.lonn.scheduleparser.parsing.tableModels;

import android.util.SparseArray;

public class DisciplinesMainPageTableModel {
	public static final TableColumn DISCIPLINE_NAME;
	public static final TableColumn DISCIPLINE_DESCRIPTION;
	public static final SparseArray<TableColumn> COLUMN_INDEX_MAP;

	private static TableColumn createColumn(Integer columnIndex) {
		TableColumn result = new TableColumn(0);
		COLUMN_INDEX_MAP.put(columnIndex, result);
		return result;
	}

	static {
		COLUMN_INDEX_MAP = new SparseArray<>();

		DISCIPLINE_NAME = createColumn(0);
		DISCIPLINE_DESCRIPTION = createColumn(1);
	}

	public static TableColumn getColumnByIndex(Integer columnIndex) {
		return COLUMN_INDEX_MAP.get(columnIndex);
	}

	private DisciplinesMainPageTableModel() {
	}
}
