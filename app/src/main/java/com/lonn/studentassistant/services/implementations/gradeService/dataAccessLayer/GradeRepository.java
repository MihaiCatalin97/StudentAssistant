package com.lonn.studentassistant.services.implementations.gradeService.dataAccessLayer;

import com.lonn.studentassistant.entities.Grade;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractRepository;
import com.lonn.studentassistant.services.implementations.gradeService.GradeService;

public class GradeRepository extends AbstractRepository<Grade>
{
    private static GradeRepository instance;

    private GradeRepository()
    {
        super(new GradeDatabaseController());
    }

    public static GradeRepository getInstance(GradeService service)
    {
        if (instance == null)
            instance = new GradeRepository();

        instance.databaseController.bindService(service);

        return instance;
    }
}
