package com.lonn.studentassistant.services.implementations.coursesService.dataAccessLayer;

import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractRepository;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.services.implementations.coursesService.CourseService;

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
