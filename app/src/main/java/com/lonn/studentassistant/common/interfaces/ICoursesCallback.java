package com.lonn.studentassistant.common.interfaces;

import com.lonn.studentassistant.entities.lists.CustomCoursesList;
import com.lonn.studentassistant.entities.Course;

public interface ICoursesCallback extends IServiceCallback
{
    void resultGetById(Course item);
    void resultGetAll(CustomCoursesList items);
}
