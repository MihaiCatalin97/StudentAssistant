package com.lonn.studentassistant.services.studentService;

import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.services.studentService.dataAccessLayer.StudentRepository;

public class StudentService extends LocalService<Student>
{
    public StudentRepository instantiateRepository()
    {
        return StudentRepository.getInstance(this);
    }
}
