package com.lonn.studentassistant.services.implementations.gradeService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.entities.Grade;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractDatabaseController;

public class GradeDatabaseController extends AbstractDatabaseController<Grade>
{
    GradeDatabaseController()
    {
        super(Grade.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("grades");
    }
}
