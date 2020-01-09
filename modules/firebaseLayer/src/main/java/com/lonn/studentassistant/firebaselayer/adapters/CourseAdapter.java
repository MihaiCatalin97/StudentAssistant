package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.DisciplineAdapter;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;

import static com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel.builder;

public class CourseAdapter extends DisciplineAdapter<Course, CourseViewModel> {
	public CourseViewModel adapt(Course course) {
		return super.adapt(builder()
				.pack(course.getPack())
				.cycleSpecialization(course.getCycleAndSpecialization())
				.build(), course);
	}

	public Course adapt(CourseViewModel courseViewModel) {
		return super.adapt(new Course()
						.setPack(courseViewModel.getPack())
						.setCycleAndSpecialization(courseViewModel.getCycleSpecialization()),
				courseViewModel);
	}
}
