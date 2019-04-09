package com.lonn.studentassistant.firebaseDatabase.students;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.common.interfaces.AbstractDatabaseController;
import com.lonn.studentassistant.entities.Student;

public class StudentDatabaseController extends AbstractDatabaseController<Student>
{
    public StudentDatabaseController()
    {
        super(Student.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("students");
    }
}
