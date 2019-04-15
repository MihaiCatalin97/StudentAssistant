package com.lonn.studentassistant.services.studentService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.common.abstractClasses.AbstractDatabaseController;
import com.lonn.studentassistant.entities.Student;

public class StudentDatabaseController extends AbstractDatabaseController<Student>
{
    StudentDatabaseController()
    {
        super(Student.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("students");
    }
}
