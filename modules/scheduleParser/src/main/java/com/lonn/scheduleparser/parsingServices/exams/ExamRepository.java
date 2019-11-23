package com.lonn.scheduleparser.parsingServices.exams;

import com.lonn.scheduleparser.parsingServices.abstractions.Repository;
import com.lonn.studentassistant.firebaselayer.models.Exam;

public class ExamRepository extends Repository<Exam> {
    private static ExamRepository instance;

    private ExamRepository() {
    }

    public static ExamRepository getInstance() {
        if (instance == null) {
            instance = new ExamRepository();
        }
        return instance;
    }

    public Exam findByScheduleLink(String scheduleLink) {
        return null;
    }
}
