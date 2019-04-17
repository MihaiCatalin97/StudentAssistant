package com.lonn.studentassistant.services.implementations.coursesService;

import com.lonn.studentassistant.services.abstractions.DatabaseService;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.services.implementations.coursesService.dataAccessLayer.CourseRepository;

public class CourseService extends DatabaseService<Course>
{
    public CourseRepository instantiateRepository()
    {
        return CourseRepository.getInstance(this);
    }
}
