package com.lonn.studentassistant.services.studentService.dataAccessLayer;

import com.lonn.studentassistant.common.abstractClasses.AbstractRepository;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.services.coursesService.dataAccessLayer.CourseDatabaseController;
import com.lonn.studentassistant.services.coursesService.dataAccessLayer.CourseRepository;
import com.lonn.studentassistant.services.studentService.StudentService;

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
