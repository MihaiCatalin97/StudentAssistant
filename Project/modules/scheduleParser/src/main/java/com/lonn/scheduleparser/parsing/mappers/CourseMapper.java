package com.lonn.scheduleparser.parsing.mappers;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.CycleSpecializationYear;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class CourseMapper extends DisciplineMapper<Course> {
	@Override
	public Course map(Element courseTableRow) {
		Course result = super.map(courseTableRow);

		if (result != null) {
			for (CycleSpecializationYear cycleSpecializationYear : getCourseCycleSpecializationYears(courseTableRow)) {
				result.addCycleSpecializationYear(cycleSpecializationYear);
			}

			return result;
		}

		return null;
	}

	@Override
	protected Boolean shouldParseRow(Element tableRow) {
		return tableRow.select("td")
				.get(1)
				.text()
				.contains("anul");
	}

	@Override
	protected Course newEntityInstance() {
		return new Course();
	}

	private List<CycleSpecializationYear> getCourseCycleSpecializationYears(Element tableRow) {
		Elements tableDivs = tableRow.select("td");
		String courseDescription = tableDivs.get(1)
				.text();
		List<CycleSpecializationYear> result = new LinkedList<>();

		for (CycleSpecializationYear cycleSpecializationYear : CycleSpecializationYear.values()) {
			if (courseDescription.toLowerCase()
					.contains(cycleSpecializationYear.toString()
							.toLowerCase())) {

				result.add(cycleSpecializationYear);
			}
		}

		return result;
	}
}
