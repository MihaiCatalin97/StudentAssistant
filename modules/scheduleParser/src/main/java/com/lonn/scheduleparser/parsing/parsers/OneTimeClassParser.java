package com.lonn.scheduleparser.parsing.parsers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lonn.scheduleparser.parsing.abstractions.Parser;
import com.lonn.scheduleparser.parsing.mappers.OneTimeClassMapper;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OneTimeClass;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OneTimeClassParser extends Parser<OneTimeClass> {
	private Date date = null;

	public OneTimeClassParser() {
		this.mapper = new OneTimeClassMapper();
	}

	protected Elements getListOfParsableElements(Document document) {
		try {
			return document.select("table")
					.get(1)
					.select("tr");
		}
		catch (Exception exception) {
			return null;
		}
	}

	protected OneTimeClass parseSingleEntity(Element parsableElement) {
		OneTimeClass parsedEntity = mapper.map(parsableElement);

		if (parsedEntity == null) {
			Date parsedDate = getDayFromMarkingRow(parsableElement);

			if (parsedDate != null) {
				date = parsedDate;
			}
		}
		else if (date != null) {
			parsedEntity.setDate(date);
		}

		return parsedEntity;
	}

	@Nullable
	private Date getDayFromMarkingRow(@NonNull Element tableRowElement) {
		try {
			String date = tableRowElement.text()
					.split(" ")[1];
			return new SimpleDateFormat("dd.MM.yyyy").parse(date);
		}
		catch (IndexOutOfBoundsException | ParseException exception) {
			return null;
		}
	}
}
