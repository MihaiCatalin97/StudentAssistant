package com.lonn.studentassistant.globalServices.coursesService;

import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.globalServices.coursesService.dataAccessLayer.CourseDatabaseController;
import com.lonn.studentassistant.globalServices.coursesService.dataAccessLayer.CourseRepository;

public class CourseService extends LocalService<Course>
{
    public void getAll()
    {
        if (serviceCallbacks != null && repository.getAll() != null)
            serviceCallbacks.resultGetAll(repository.getAll());
    }

    public CourseRepository instantiateRepository()
    {
        return new CourseRepository(new CourseDatabaseController(this));
    }
}
