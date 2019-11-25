package com.lonn.scheduleparser.parsingServices.tableParseResults;

import com.lonn.scheduleparser.parsingServices.tableModels.TableColumn;

import java.util.Map;

class ParseResult {
	protected Map<TableColumn, TableDivElement> columnValueMap;

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

	protected ParseResult(){
	}
}
