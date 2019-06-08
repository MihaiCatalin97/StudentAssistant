package com.lonn.studentassistant.activities.implementations.student.managers;

import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.implementations.ProfessorsScrollViewLayout;
import com.lonn.studentassistant.entities.Professor;

import java.util.List;

public class StudentActivityProfessorManager
{
    private ProfessorsScrollViewLayout professorsLayout;

    public StudentActivityProfessorManager(ProfessorsScrollViewLayout professorsLayout)
    {
        this.professorsLayout = professorsLayout;
    }

    public void notifyGetAll(List<Professor> professors)
    {
        professorsLayout.add(professors);
    }

    public void notifyEdit(Professor professor)
    {
        professorsLayout.update(professor);
    }

    public void notifyDelete(Professor professor)
    {
        professorsLayout.delete(professor);
    }
}
