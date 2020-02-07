package com.lonn.scheduleparser.parsing.mappers;

import com.lonn.scheduleparser.parsing.abstractions.Mapper;
import com.lonn.scheduleparser.parsing.tableParseResults.DisciplineScheduleParseResult;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType;

import org.jsoup.nodes.Element;

import static com.lonn.scheduleparser.parsing.Utils.hourStringToInteger;
import static com.lonn.scheduleparser.parsing.Utils.splitByCommon;
import static com.lonn.scheduleparser.parsing.tableModels.DisciplineScheduleTableModel.FROM_HOUR;
import static com.lonn.scheduleparser.parsing.tableModels.DisciplineScheduleTableModel.PROFESSORS;
import static com.lonn.scheduleparser.parsing.tableModels.DisciplineScheduleTableModel.ROOMS;
import static com.lonn.scheduleparser.parsing.tableModels.DisciplineScheduleTableModel.STUDENT_GROUPS;
import static com.lonn.scheduleparser.parsing.tableModels.DisciplineScheduleTableModel.TO_HOUR;
import static com.lonn.scheduleparser.parsing.tableModels.DisciplineScheduleTableModel.TYPE;
import static com.lonn.scheduleparser.parsing.tableParseResults.DisciplineScheduleParseResult.fromRow;

public abstract class ScheduleClassMapper<T extends ScheduleClass> extends Mapper<T> {
	public T map(Element tableRow) {
		if (isParsableRow(tableRow)) {
			return (T) map(fromRow(tableRow));
		}

		return null;
	}

	protected abstract Boolean isParsableRow(Element tableRow);

	protected abstract T newMappedEntity();

	public ScheduleClass map(DisciplineScheduleParseResult parseResult) {
		ScheduleClass result = newMappedEntity()
				.setStartHour(hourStringToInteger(parseResult.getTextOfColumn(FROM_HOUR)))
				.setEndHour(hourStringToInteger(parseResult.getTextOfColumn(TO_HOUR)))
				.setType(ScheduleClassType.valueOfRoString(parseResult.getTextOfColumn(TYPE)))
				.setGroups(splitByCommon(parseResult.getTextOfColumn(STUDENT_GROUPS)))
				.setProfessors(splitByCommon(parseResult.getLinkOfColumn(PROFESSORS)))
				.setRooms(splitByCommon(parseResult.getTextOfColumn(ROOMS)));

		return result;
	}
}
