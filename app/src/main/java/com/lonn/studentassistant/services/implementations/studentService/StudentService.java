package com.lonn.studentassistant.services.implementations.studentService;

import com.lonn.studentassistant.services.abstractions.DatabaseService;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.services.implementations.studentService.dataAccessLayer.StudentRepository;

public class StudentService extends DatabaseService<Student>
{
    public StudentRepository instantiateRepository()
    {
        return StudentRepository.getInstance(this);
    }
}
