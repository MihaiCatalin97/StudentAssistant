package com.lonn.studentassistant.globalServices.studentService;

import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.globalServices.studentService.dataAccessLayer.StudentDatabaseController;
import com.lonn.studentassistant.globalServices.studentService.dataAccessLayer.StudentRepository;

public class StudentService extends LocalService<Student>
{
    public void getAll()
    {
        if (serviceCallbacks != null && repository.getAll() != null)
            serviceCallbacks.resultGetAll(repository.getAll());
    }

    public StudentRepository instantiateRepository()
    {
        return new StudentRepository(new StudentDatabaseController());
    }
}
