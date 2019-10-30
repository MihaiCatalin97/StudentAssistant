package com.lonn.studentassistant.firebaselayer.requests;

import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.Exam;
import com.lonn.studentassistant.firebaselayer.models.Grade;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.Professor;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.models.Student;
import com.lonn.studentassistant.firebaselayer.models.User;

public enum DatabaseTable {
    COURSES(Course.class),
    EXAMS(Exam.class),
    GRADES(Grade.class),
    OTHER_ACTIVITIES(OtherActivity.class),
    PROFESSORS(Professor.class),
    SCHEDULE_CLASSES(ScheduleClass.class),
    STUDENTS(Student.class),
    USERS(User.class);

    private Class<? extends BaseEntity> tableClass;

    DatabaseTable(Class<? extends BaseEntity> tableClass) {
        this.tableClass = tableClass;
    }

    public Class<? extends BaseEntity> getTableClass() {
        return tableClass;
    }

    public boolean isGlobalTable() {
        return true;
    }

    public boolean isUserSpecificTable() {
        return !isGlobalTable();
    }
}
