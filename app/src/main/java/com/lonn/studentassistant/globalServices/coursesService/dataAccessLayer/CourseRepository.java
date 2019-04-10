package com.lonn.studentassistant.globalServices.coursesService.dataAccessLayer;

import com.lonn.studentassistant.common.abstractClasses.AbstractRepository;
import com.lonn.studentassistant.entities.Course;

public class CourseRepository extends AbstractRepository<Course>
{
    public CourseRepository(CourseDatabaseController controller)
    {
        super(controller);
    }
}
