package com.lonn.studentassistant.services.implementations.professorService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractDatabaseController;

public class ProfessorDatabaseController extends AbstractDatabaseController<Professor>
{
    ProfessorDatabaseController()
    {
        super(Professor.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("professors");
    }
}
