package com.lonn.studentassistant.services.implementations.otherActivityService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractDatabaseController;

class OtherActivityDatabaseController extends AbstractDatabaseController<OtherActivity>
{
    OtherActivityDatabaseController()
    {
        super(OtherActivity.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("otherActivities");
    }
}
