package com.lonn.studentassistant.firebaselayer.database;

import com.lonn.studentassistant.firebaselayer.models.Administrator;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.Exam;
import com.lonn.studentassistant.firebaselayer.models.Grade;
import com.lonn.studentassistant.firebaselayer.models.IdentificationHash;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.Professor;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.models.Student;
import com.lonn.studentassistant.firebaselayer.models.User;
import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

public class DatabaseTableContainer {
    public static final DatabaseTable<Administrator> ADMINISTRATORS;
    public static final DatabaseTable<Course> COURSES;
    public static final DatabaseTable<Exam> EXAMS;
    public static final DatabaseTable<Grade> GRADES;
    public static final DatabaseTable<OtherActivity> OTHER_ACTIVITIES;
    public static final DatabaseTable<Professor> PROFESSORS;
    public static final DatabaseTable<ScheduleClass> SCHEDULE_CLASSES;
    public static final DatabaseTable<Student> STUDENTS;
    public static final DatabaseTable<User> USERS;
    public static final DatabaseTable<IdentificationHash> IDENTIFICATION_HASHES;

    static {
        ADMINISTRATORS = new DatabaseTable<>(Administrator.class, "Administrators");
        COURSES = new DatabaseTable<>(Course.class, "Courses");
        EXAMS = new DatabaseTable<>(Exam.class, "Exams");
        GRADES = new DatabaseTable<>(Grade.class, "Grades");
        OTHER_ACTIVITIES = new DatabaseTable<>(OtherActivity.class, "OtherActivities");
        PROFESSORS = new DatabaseTable<>(Professor.class, "Professors");
        SCHEDULE_CLASSES = new DatabaseTable<>(ScheduleClass.class, "ScheduleClasses");
        STUDENTS = new DatabaseTable<>(Student.class, "Students");
        USERS = new DatabaseTable<>(User.class, "Users");
        IDENTIFICATION_HASHES = new DatabaseTable<>(IdentificationHash.class, "IdentificationHashes");
    }

    public static <T extends BaseEntity> DatabaseTable valueOf(Class<T> entityClass) {
        if (entityClass.equals(Administrator.class)) {
            return ADMINISTRATORS;
        }
        if (entityClass.equals(Course.class)) {
            return COURSES;
        }
        if (entityClass.equals(Exam.class)) {
            return EXAMS;
        }
        if (entityClass.equals(Grade.class)) {
            return GRADES;
        }
        if (entityClass.equals(OtherActivity.class)) {
            return OTHER_ACTIVITIES;
        }
        if (entityClass.equals(Professor.class)) {
            return PROFESSORS;
        }
        if (entityClass.equals(ScheduleClass.class)) {
            return SCHEDULE_CLASSES;
        }
        if (entityClass.equals(Student.class)) {
            return STUDENTS;
        }
        if (entityClass.equals(User.class)) {
            return USERS;
        }
        if (entityClass.equals(IdentificationHash.class)) {
            return IDENTIFICATION_HASHES;
        }
        return null;
    }
}
