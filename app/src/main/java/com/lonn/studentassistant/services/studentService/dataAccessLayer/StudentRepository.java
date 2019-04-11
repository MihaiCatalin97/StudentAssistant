package com.lonn.studentassistant.services.studentService.dataAccessLayer;

import com.lonn.studentassistant.common.abstractClasses.AbstractRepository;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Student;

public class StudentRepository extends AbstractRepository<Student>
{
    public StudentRepository(StudentDatabaseController controller)
    {
        super(controller);
    }
}
