package com.lonn.studentassistant.services.implementations.studentService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractDatabaseController;
import com.lonn.studentassistant.entities.Student;

class StudentDatabaseController extends AbstractDatabaseController<Student>
{
    StudentDatabaseController()
    {
        super(Student.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("students");
    }
}
