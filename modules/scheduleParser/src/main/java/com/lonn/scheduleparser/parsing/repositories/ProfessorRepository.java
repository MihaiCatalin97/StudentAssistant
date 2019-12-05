package com.lonn.scheduleparser.parsing.repositories;

import com.lonn.scheduleparser.parsing.abstractions.Repository;
import com.lonn.studentassistant.firebaselayer.entities.Professor;

public class ProfessorRepository extends Repository<Professor> {
    private static ProfessorRepository instance;

    private ProfessorRepository() {
    }

    public static ProfessorRepository getInstance() {
        if (instance == null) {
            instance = new ProfessorRepository();
        }
        return instance;
    }

    public Professor findByScheduleLink(String scheduleLink) {
        for (Professor professor : entities) {
            if (professor.getScheduleLink() != null &&
                    professor.getScheduleLink().equals(scheduleLink)) {
                return professor;
            }
        }
        return null;
    }
}
