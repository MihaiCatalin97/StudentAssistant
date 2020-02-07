package com.lonn.scheduleparser.parsing;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class Utils {
	private static final Logger LOGGER = Logger.ofClass(Utils.class);

	public static Integer hourStringToInteger(String hourString) {
		try {
			return Integer.parseInt(hourString.replace(":", ""));
		}
		catch (NumberFormatException exception) {
			LOGGER.error("Error while parsing hour", exception);
			return null;
		}
	}

	public static Integer intStringToInteger(String intString) {
		try {
			return Integer.parseInt(intString);
		}
		catch (NumberFormatException exception) {
			LOGGER.error("Error while parsing integer", exception);
			return null;
		}
	}

	public static List<String> splitByCommon(String stringWithCommons) {
		if (stringWithCommons == null)
			return emptyList();
		return asList(stringWithCommons.replace(" ", "")
				.split(","));
	}
}
