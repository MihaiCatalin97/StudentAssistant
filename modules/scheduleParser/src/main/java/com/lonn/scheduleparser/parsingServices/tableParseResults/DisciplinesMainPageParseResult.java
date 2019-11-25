package com.lonn.scheduleparser.parsingServices.tableParseResults;

import com.lonn.scheduleparser.parsingServices.tableModels.TableColumn;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

import static com.lonn.scheduleparser.parsingServices.tableModels.DisciplinesMainPageTableModel.getColumnByIndex;
import static com.lonn.scheduleparser.parsingServices.tableParseResults.TableDivElement.fromTableDiv;

public class DisciplinesMainPageParseResult extends ParseResult {
	public static DisciplinesMainPageParseResult fromRow(Element tableRow) {
		DisciplinesMainPageParseResult result = new DisciplinesMainPageParseResult();
		Elements tableDivs = tableRow.select("td");

		for (int columnIndex = 0; columnIndex < tableDivs.size(); columnIndex++) {
			TableColumn columnAtIndex = getColumnByIndex(columnIndex);

			result.columnValueMap.put(columnAtIndex,
					fromTableDiv(tableDivs.get(columnIndex)));
		}

		return result;
	}

	private DisciplinesMainPageParseResult() {
		columnValueMap = new HashMap<>();
	}
}
