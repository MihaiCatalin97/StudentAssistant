package com.lonn.studentassistant.firebaseDatabase.students;

import com.lonn.studentassistant.common.interfaces.IRepository;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.entities.User;

import java.util.List;
import java.util.UUID;

public class StudentRepository extends IRepository<Student>
{
    public StudentRepository(StudentDatabaseController controller)
    {
        super(controller);
    }

    public Student getById(Object numarMatricol)
    {
        for (Student u : items)
        {
            if (u.numarMatricol.equals(numarMatricol))
                return u;
        }

        return null;
    }

    public List<Student> getAll()
    {
        return items;
    }

    public void update(Student student)
    {
        databaseController.update(student);
    }

    public void add(Student student)
    {
        databaseController.add(student);
    }

    public void remove(Student student)
    {
        databaseController.remove(student);
    }
}
