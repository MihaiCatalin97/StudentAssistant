package com.lonn.studentassistant.firebaseDatabase.students;

import com.lonn.studentassistant.common.interfaces.AbstractRepository;
import com.lonn.studentassistant.entities.Student;

public class StudentRepository extends AbstractRepository<Student>
{
    public StudentRepository(StudentDatabaseController controller)
    {
        super(controller);
    }
}
