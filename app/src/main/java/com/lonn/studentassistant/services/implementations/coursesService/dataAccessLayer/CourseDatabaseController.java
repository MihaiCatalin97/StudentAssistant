package com.lonn.studentassistant.services.implementations.coursesService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractDatabaseController;
import com.lonn.studentassistant.entities.Course;

class CourseDatabaseController extends AbstractDatabaseController<Course>
{
    CourseDatabaseController()
    {
        super(Course.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("courses");
    }
}
