package com.lonn.scheduleparser.parsing.tableParseResults;

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
				textBuilder.append(anchorsInsideDiv.get(anchorIndex).text());
				anchorBuilder.append(anchorsInsideDiv.get(anchorIndex).attr("abs:href"));

				if (anchorIndex + 1 < anchorsInsideDiv.size()) {
					textBuilder.append(", ");
					anchorBuilder.append(", ");
				}
			}

			result.elementText = textBuilder.toString();
			result.elementHref = anchorBuilder.toString();
		}
		else {
			result.elementText = tableDiv.text();
		}

		return result;
	}

	private TableDivElement() {
	}
}
