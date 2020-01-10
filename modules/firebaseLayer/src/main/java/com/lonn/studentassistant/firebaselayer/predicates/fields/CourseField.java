package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.entities.Course;

public class CourseField<T> extends BaseEntityField<Course, T> {
	public static CourseField<String> COURSE_NAME = new CourseField<>("disciplineName");
	public static CourseField<Integer> YEAR = new CourseField<>("year");
	public static CourseField<Integer> SEMESTER = new CourseField<>("semester");
	public static CourseField<Integer> PACK = new CourseField<>("pack");

	private CourseField(String fieldName) {
		super(fieldName);
	}
}