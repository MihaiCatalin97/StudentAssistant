package com.lonn.scheduleparser.parsingServices.tableParseResults;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableDivElement {
	private String elementText;
	private String elementHref;

	public static TableDivElement fromTableDiv(Element tableDiv) {
		TableDivElement result = new TableDivElement();
		Elements anchorsInsideDiv = tableDiv.select("a");

		if (anchorsInsideDiv != null && anchorsInsideDiv.size() > 0) {
			StringBuilder textBuilder = new StringBuilder();
			StringBuilder anchorBuilder = new StringBuilder();

			for (int anchorIndex = 0; anchorIndex < anchorsInsideDiv.size(); anchorIndex++) {
				textBuilder.append(anchorsInsideDiv.text());
				anchorBuilder.append(anchorsInsideDiv.attr("abs:href"));

				if (anchorIndex + 1 < anchorsInsideDiv.size()) {
					textBuilder.append(", ");
					anchorBuilder.append(", ");
				}
			}
		}
		else {
			result.elementText = tableDiv.text();
		}

		return result;
	}

	private TableDivElement() {
	}
}
