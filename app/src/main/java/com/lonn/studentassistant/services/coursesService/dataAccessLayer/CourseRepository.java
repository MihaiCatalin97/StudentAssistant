package com.lonn.studentassistant.services.coursesService.dataAccessLayer;

import com.lonn.studentassistant.common.abstractClasses.AbstractRepository;
import com.lonn.studentassistant.entities.Course;

import java.util.ArrayList;

public class CourseRepository extends AbstractRepository<Course>
{
    public CourseRepository(CourseDatabaseController controller)
    {
        super(controller);
    }
}
