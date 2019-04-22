package com.lonn.studentassistant.services.implementations.examService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.entities.Exam;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractDatabaseController;

public class ExamDatabaseController extends AbstractDatabaseController<Exam>
{
    ExamDatabaseController()
    {
        super(Exam.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("exams");
    }
}
