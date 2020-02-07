package com.lonn.scheduleparser.parsing.tableParseResults;

import com.lonn.scheduleparser.parsing.tableModels.TableColumn;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

import static com.lonn.scheduleparser.parsing.tableModels.DisciplinesMainPageTableModel.getColumnByIndex;
import static com.lonn.scheduleparser.parsing.tableParseResults.TableDivElement.fromTableDiv;

public class DisciplinesMainPageParseResult extends ParseResult {
	private DisciplinesMainPageParseResult() {
		columnValueMap = new HashMap<>();
	}

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
}
