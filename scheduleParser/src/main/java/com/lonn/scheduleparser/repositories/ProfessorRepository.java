package com.lonn.scheduleparser.repositories;

import com.lonn.scheduleparser.mergers.ProfessorMerger;
import com.lonn.studentassistant.firebaselayer.models.Professor;

public class ProfessorRepository extends Repository<Professor> {
    private static ProfessorRepository instance;

    private ProfessorRepository() {
        merger = new ProfessorMerger();
    }

    public static ProfessorRepository getInstance() {
        if (instance == null) {
            instance = new ProfessorRepository();
        }
        return instance;
    }
}
