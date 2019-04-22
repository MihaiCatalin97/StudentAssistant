package com.lonn.studentassistant.services.implementations.scheduleService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractDatabaseController;

public class ScheduleDatabaseController extends AbstractDatabaseController<ScheduleClass>
{
    ScheduleDatabaseController()
    {
        super(ScheduleClass.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("schedule");
    }
}
