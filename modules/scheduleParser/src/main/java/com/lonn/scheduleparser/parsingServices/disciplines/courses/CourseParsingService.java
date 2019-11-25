package com.lonn.scheduleparser.parsingServices.disciplines.courses;

import com.lonn.scheduleparser.parsingServices.abstractions.ParsingService;
import com.lonn.scheduleparser.parsingServices.disciplines.common.DisciplineParsingService;
import com.lonn.studentassistant.firebaselayer.entities.Course;

import java.util.List;
import java.util.concurrent.Future;

import static com.lonn.scheduleparser.parsingServices.ScheduleConstants.COURSES_PAGE;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

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
