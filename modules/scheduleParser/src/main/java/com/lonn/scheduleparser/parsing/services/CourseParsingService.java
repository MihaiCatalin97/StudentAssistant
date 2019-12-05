package com.lonn.scheduleparser.parsing.services;

import com.lonn.scheduleparser.parsing.parsers.CourseParser;
import com.lonn.scheduleparser.parsing.repositories.CourseRepository;
import com.lonn.studentassistant.firebaselayer.entities.Course;

public class CourseParsingService extends DisciplineParsingService<Course> {
	private static CourseParsingService instance;

	private CourseParsingService() {
		repository = CourseRepository.getInstance();
		parser = new CourseParser();
	}

	public static CourseParsingService getInstance() {
		if (instance == null) {
			instance = new CourseParsingService();
		}
		return instance;
	}
}
