package com.lonn.studentassistant.services.coursesService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.common.abstractClasses.AbstractDatabaseController;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.services.coursesService.CourseService;

public class CourseDatabaseController extends AbstractDatabaseController<Course>
{
    public CourseDatabaseController()
    {
        super(Course.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("courses");
    }
}
