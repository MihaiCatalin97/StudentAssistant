package com.lonn.scheduleparser.parsing.repositories;

import com.lonn.scheduleparser.parsing.abstractions.Repository;
import com.lonn.studentassistant.firebaselayer.entities.Course;

public class CourseRepository extends Repository<Course> {
	private static CourseRepository instance;

	private CourseRepository() {
	}

	public static CourseRepository getInstance() {
		if (instance == null) {
			instance = new CourseRepository();
		}
		return instance;
	}

	public Course findByScheduleLink(String scheduleLink) {
		for (Course course : entities) {
			if (course.getScheduleLink() != null &&
					course.getScheduleLink().equals(scheduleLink)) {
				return course;
			}
		}
		return null;
	}
}
