package com.lonn.scheduleparser.parsing;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.jsoup.Jsoup.connect;
import static org.jsoup.Jsoup.parse;

public class DocumentHolder {
	private static final Logger LOGGER = Logger.ofClass(DocumentHolder.class);

	private static Map<String, Document> documentMap;

	static {
		documentMap = new HashMap<>();
	}

	public static Document getDocumentForLink(String link) {
		return getCachedDocumentOrRead(link);
	}

	private static Document getCachedDocumentOrRead(String pageLink) {
		Document document = documentMap.get(pageLink);

		if (document == null) {
			document = readDocumentAtLink(pageLink);
		}

		if (document != null) {
			documentMap.put(pageLink, document);
		}

		return document;
	}

	private static Document readDocumentAtLink(String pageLink) {
		try {
			return connect(pageLink).get();

		}
		catch (IOException exception) {
			try {
				Response execute = connect(pageLink).execute();
				return parse(execute.body(), pageLink);
			}
			catch (IOException innerException) {
				LOGGER.error("Error while reading document", exception);
				return null;
			}
		}
	}
}
