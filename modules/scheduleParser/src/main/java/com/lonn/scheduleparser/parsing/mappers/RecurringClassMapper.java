package com.lonn.scheduleparser.parsing.mappers;

import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;

import org.jsoup.nodes.Element;

public class RecurringClassMapper extends ScheduleClassMapper<RecurringClass> {
    public RecurringClass map(Element tableRow) {
        RecurringClass result = super.map(tableRow);

        if (result != null) {
            return (RecurringClass)
                    result.setParity(getParityFromRow(tableRow));
        }
        return null;
    }

    @Override
    protected Boolean isParsableRow(Element tableRow) {
        return tableRow.select("td")
                .size() == 9;
    }

    @Override
    protected RecurringClass newMappedEntity() {
        return new RecurringClass();
    }

    private String getParityFromRow(Element tableRow) {
        return tableRow.select("td")
                .get(7)
                .text();
    }
}
