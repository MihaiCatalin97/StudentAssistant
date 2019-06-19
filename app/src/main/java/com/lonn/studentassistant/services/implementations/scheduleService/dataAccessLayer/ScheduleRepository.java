package com.lonn.studentassistant.services.implementations.scheduleService.dataAccessLayer;

import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractRepository;
import com.lonn.studentassistant.services.implementations.scheduleService.ScheduleClassService;

public class ScheduleRepository extends AbstractRepository<ScheduleClass>
{
    private static ScheduleRepository instance;

    private ScheduleRepository()
    {
        super(new ScheduleDatabaseController());
    }

    public static ScheduleRepository getInstance(ScheduleClassService service)
    {
        if (instance == null)
            instance = new ScheduleRepository();

        instance.databaseController.bindService(service);

        return instance;
    }
}

