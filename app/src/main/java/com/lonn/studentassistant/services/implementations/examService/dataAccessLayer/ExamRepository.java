package com.lonn.studentassistant.services.implementations.examService.dataAccessLayer;

import com.lonn.studentassistant.entities.Exam;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractRepository;
import com.lonn.studentassistant.services.implementations.examService.ExamService;
import com.lonn.studentassistant.services.implementations.scheduleService.ScheduleService;
import com.lonn.studentassistant.services.implementations.scheduleService.dataAccessLayer.ScheduleDatabaseController;
import com.lonn.studentassistant.services.implementations.scheduleService.dataAccessLayer.ScheduleRepository;

public class ExamRepository extends AbstractRepository<Exam>
{
    private static ExamRepository instance;

    private ExamRepository()
    {
        super(new ExamDatabaseController());
    }

    public static ExamRepository getInstance(ExamService service)
    {
        if (instance == null)
            instance = new ExamRepository();

        instance.databaseController.bindService(service);

        return instance;
    }
}
