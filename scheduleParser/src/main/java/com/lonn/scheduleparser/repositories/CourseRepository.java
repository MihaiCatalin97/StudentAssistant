package com.lonn.scheduleparser.repositories;

import com.lonn.scheduleparser.mergers.CourseMerger;
import com.lonn.studentassistant.firebaselayer.models.Course;

public class CourseRepository extends Repository<Course> {
    private static CourseRepository instance;

    private CourseRepository() {
        merger = new CourseMerger();
    }

    public static CourseRepository getInstance() {
        if (instance == null) {
            instance = new CourseRepository();
        }
        return instance;
    }

    @Override
    public boolean add(Course course) {
        return super.add(course);
    }

    public Course findByScheduleLink(String scheduleLink) {
        for (Course course : entities) {
            if (course.getScheduleLink().equals(scheduleLink)) {
                return course;
            }
        }
        return null;
    }
}
