package com.lonn.studentassistant.services.coursesService;

import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.services.coursesService.dataAccessLayer.CourseRepository;

public class CourseService extends LocalService<Course>
{

    public CourseRepository instantiateRepository()
    {
        return CourseRepository.getInstance(this);
    }
}
