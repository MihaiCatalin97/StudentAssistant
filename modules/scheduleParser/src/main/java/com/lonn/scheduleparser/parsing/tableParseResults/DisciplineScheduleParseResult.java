package com.lonn.scheduleparser.parsing.tableParseResults;

import com.lonn.scheduleparser.parsing.tableModels.TableColumn;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

import static com.lonn.scheduleparser.parsing.tableModels.DisciplineScheduleTableModel.getColumnByIndex;
import static com.lonn.scheduleparser.parsing.tableParseResults.TableDivElement.fromTableDiv;

public class DisciplineScheduleParseResult extends ParseResult {
	private DisciplineScheduleParseResult() {
		columnValueMap = new HashMap<>();
	}

	public static DisciplineScheduleParseResult fromRow(Element tableRow) {
		DisciplineScheduleParseResult result = new DisciplineScheduleParseResult();
		Elements tableDivs = tableRow.select("td");

		for (int columnIndex = 0; columnIndex < tableDivs.size(); columnIndex++) {
			TableColumn columnAtIndex = getColumnByIndex(columnIndex);

			result.columnValueMap.put(columnAtIndex,
					fromTableDiv(tableDivs.get(columnIndex)));
		}

		return result;
	}
}
