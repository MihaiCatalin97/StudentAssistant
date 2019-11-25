package com.lonn.scheduleparser.parsingServices.abstractions;

import com.lonn.scheduleparser.parsingServices.Logger;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.lonn.scheduleparser.parsingServices.DocumentHolder.getDocumentForLink;
import static java.util.Collections.emptyList;

public abstract class ParsingService<T extends BaseEntity> {
	private static final Logger LOGGER = Logger.ofClass(ParsingService.class);
	protected Repository<T> repository;
	protected Parser<T> parser;

	public List<T> getAll() {
		try {
			return parse().get();
		}
		catch (InterruptedException | ExecutionException exception) {
			LOGGER.error("Error while parsing", exception);
			return emptyList();
		}
	}

	public void update(T entity) {
		repository.update(entity);
	}

	public T getByScheduleLink(String scheduleLink) {
		return repository.findByScheduleLink(scheduleLink);
	}

	public void deleteAll() {
		repository.clearAll();
	}

	protected abstract Future<List<T>> parse();

	protected List<T> parseSinglePage(String pageLink) {
		Document document = getDocumentForLink(pageLink);

		if (document != null) {
			return parser.parse(document);
		}

		return null;
	}
}
