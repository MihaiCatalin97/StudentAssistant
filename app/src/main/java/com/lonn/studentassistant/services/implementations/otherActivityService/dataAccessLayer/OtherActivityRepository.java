package com.lonn.studentassistant.services.implementations.otherActivityService.dataAccessLayer;

import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractRepository;
import com.lonn.studentassistant.services.implementations.otherActivityService.OtherActivityService;

public class OtherActivityRepository extends AbstractRepository<OtherActivity>
{
    private static OtherActivityRepository instance;

    private OtherActivityRepository()
    {
        super(new OtherActivityDatabaseController());
    }

    public OtherActivity getByName(String activityName)
    {
        for(OtherActivity activity : getAll())
        {
            if(activity.activityName.equals(activityName))
                return activity;
        }

        return null;
    }

    public static OtherActivityRepository getInstance(OtherActivityService service)
    {
        if (instance == null)
            instance = new OtherActivityRepository();

        instance.databaseController.bindService(service);

        return instance;
    }
}
