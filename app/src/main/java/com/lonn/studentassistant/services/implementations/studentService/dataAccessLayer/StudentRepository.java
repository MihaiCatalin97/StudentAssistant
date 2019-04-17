package com.lonn.studentassistant.services.implementations.studentService.dataAccessLayer;

import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractRepository;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.services.implementations.studentService.StudentService;

public class StudentRepository extends AbstractRepository<Student>
{
    private static StudentRepository instance;

    private StudentRepository()
    {
        super(new StudentDatabaseController());
    }

    public static StudentRepository getInstance(StudentService service)
    {
        if (instance == null)
            instance = new StudentRepository();

        instance.databaseController.bindService(service);

        return instance;
    }
}
