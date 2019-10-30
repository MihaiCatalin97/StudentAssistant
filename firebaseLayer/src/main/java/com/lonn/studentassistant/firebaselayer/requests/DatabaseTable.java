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

    public static DatabaseTable valueOf(Class<? extends BaseEntity> tableClass) {
        if (tableClass.equals(Course.class)) {
            return COURSES;
        }
        if (tableClass.equals(Exam.class)) {
            return EXAMS;
        }
        if (tableClass.equals(Grade.class)) {
            return GRADES;
        }
        if (tableClass.equals(OtherActivity.class)) {
            return OTHER_ACTIVITIES;
        }
        if (tableClass.equals(Professor.class)) {
            return PROFESSORS;
        }
        if (tableClass.equals(ScheduleClass.class)) {
            return SCHEDULE_CLASSES;
        }
        if (tableClass.equals(Student.class)) {
            return STUDENTS;
        }

        return USERS;
    }

    public Class<? extends BaseEntity> getTableClass() {
        return tableClass;
    }
}
