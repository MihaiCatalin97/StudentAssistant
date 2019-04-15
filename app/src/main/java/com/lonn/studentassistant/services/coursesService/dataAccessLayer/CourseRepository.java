package com.lonn.studentassistant.services.coursesService.dataAccessLayer;

import com.lonn.studentassistant.common.abstractClasses.AbstractRepository;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.services.coursesService.CourseService;

import java.util.ArrayList;

public class CourseRepository extends AbstractRepository<Course>
{
    private static CourseRepository instance;

    private CourseRepository()
    {
        super(new CourseDatabaseController());
    }

    public static CourseRepository getInstance(CourseService service)
    {
        if (instance == null)
            instance = new CourseRepository();

        instance.databaseController.bindService(service);

        return instance;
    }
}
