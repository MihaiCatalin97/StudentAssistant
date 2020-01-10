package com.lonn.scheduleparser.parsing.tableParseResults;

import com.lonn.scheduleparser.parsing.tableModels.TableColumn;

import java.util.Map;

class ParseResult {
	protected Map<TableColumn, TableDivElement> columnValueMap;

	protected ParseResult() {
	}

	public String getTextOfColumn(TableColumn tableColumn) {
		TableDivElement parsedElement = columnValueMap.get(tableColumn);

		if (parsedElement != null) {
			return parsedElement.getElementText();
		}
		return null;
	}

	public String getLinkOfColumn(TableColumn tableColumn) {
		TableDivElement parsedElement = columnValueMap.get(tableColumn);

		if (parsedElement != null) {
			return parsedElement.getElementHref();
		}
		return null;
	}
}
