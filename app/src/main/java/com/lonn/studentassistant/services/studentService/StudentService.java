package com.lonn.studentassistant.services.studentService;

import com.lonn.studentassistant.entities.lists.CustomStudentList;
import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;
import com.lonn.studentassistant.common.interfaces.IStudentCallback;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.services.studentService.dataAccessLayer.StudentDatabaseController;
import com.lonn.studentassistant.services.studentService.dataAccessLayer.StudentRepository;

public class StudentService extends LocalService<Student>
{
    public void getAll()
    {
        if (repository.getAll() != null)
            for (IServiceCallback callback : serviceCallbacks)
                ((IStudentCallback)callback).resultGetAll(new CustomStudentList(repository.getAll()));
    }

    public StudentRepository instantiateRepository()
    {
        return new StudentRepository(new StudentDatabaseController());
    }
}
