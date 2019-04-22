package com.lonn.studentassistant.services.implementations.professorService.dataAccessLayer;

import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractRepository;
import com.lonn.studentassistant.services.implementations.professorService.ProfessorService;

public class ProfessorRepository extends AbstractRepository<Professor>
{
    private static ProfessorRepository instance;

    private ProfessorRepository()
    {
        super(new ProfessorDatabaseController());
    }

    public static ProfessorRepository getInstance(ProfessorService service)
    {
        if (instance == null)
            instance = new ProfessorRepository();

        instance.databaseController.bindService(service);

        return instance;
    }
}
