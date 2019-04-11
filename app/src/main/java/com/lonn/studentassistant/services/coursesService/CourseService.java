package com.lonn.studentassistant.services.coursesService;

import com.lonn.studentassistant.entities.lists.CustomCoursesList;
import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.common.interfaces.ICoursesCallback;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.services.coursesService.dataAccessLayer.CourseDatabaseController;
import com.lonn.studentassistant.services.coursesService.dataAccessLayer.CourseRepository;

public class CourseService extends LocalService<Course>
{
    public void getAll()
    {
        if (repository.getAll() != null)
            for (IServiceCallback callback : serviceCallbacks)
                ((ICoursesCallback)callback).resultGetAll(new CustomCoursesList(repository.getAll()));
    }

    public CourseRepository instantiateRepository()
    {
        return new CourseRepository(new CourseDatabaseController(this));
    }
}
