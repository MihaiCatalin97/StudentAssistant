package com.lonn.studentassistant.common.interfaces;

import com.lonn.studentassistant.entities.lists.CustomStudentList;
import com.lonn.studentassistant.entities.Student;

public interface IStudentCallback extends IServiceCallback
{
    void resultGetById(Student item);
    void resultGetAll(CustomStudentList items);
}
