package com.lonn.studentassistant.firebaselayer.database;

import com.lonn.studentassistant.firebaselayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.entities.Grade;
import com.lonn.studentassistant.firebaselayer.entities.IdentificationHash;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.User;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;

public class DatabaseTableContainer {
    public static final DatabaseTable<Administrator> ADMINISTRATORS;
    public static final DatabaseTable<Course> COURSES;
    public static final DatabaseTable<Grade> GRADES;
    public static final DatabaseTable<OtherActivity> OTHER_ACTIVITIES;
    public static final DatabaseTable<Professor> PROFESSORS;
    public static final DatabaseTable<RecurringClass> RECURRING_CLASSES;
    public static final DatabaseTable<OneTimeClass> ONE_TIME_CLASSES;
    public static final DatabaseTable<Student> STUDENTS;
    public static final DatabaseTable<User> USERS;
    public static final DatabaseTable<IdentificationHash> IDENTIFICATION_HASHES;
    public static final DatabaseTable<FileMetadata> FILE_METADATA;
    public static final DatabaseTable<FileMetadata> FILE_CONTENT;

    static {
        ADMINISTRATORS = new DatabaseTable<>(Administrator.class, "Administrators");
        COURSES = new DatabaseTable<>(Course.class, "Courses");
        GRADES = new DatabaseTable<>(Grade.class, "Grades");
        OTHER_ACTIVITIES = new DatabaseTable<>(OtherActivity.class, "OtherActivities");
        PROFESSORS = new DatabaseTable<>(Professor.class, "Professors");
        RECURRING_CLASSES = new DatabaseTable<>(RecurringClass.class, "Schedule/Recurring");
        ONE_TIME_CLASSES = new DatabaseTable<>(OneTimeClass.class, "Schedule/OneTime");
        STUDENTS = new DatabaseTable<>(Student.class, "Students");
        USERS = new DatabaseTable<>(User.class, "Users");
        IDENTIFICATION_HASHES = new DatabaseTable<>(IdentificationHash.class, "IdentificationHashes");
        FILE_METADATA = new DatabaseTable<>(FileMetadata.class, "Files/Metadata");
        FILE_CONTENT = new DatabaseTable<>(FileMetadata.class, "Files/Content");
    }

    public static <T extends BaseEntity> DatabaseTable valueOf(Class<T> entityClass) {
        if (entityClass.equals(Administrator.class)) {
            return ADMINISTRATORS;
        }
        if (entityClass.equals(Course.class)) {
            return COURSES;
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
        if (entityClass.equals(OneTimeClass.class)) {
            return ONE_TIME_CLASSES;
        }
        if (entityClass.equals(RecurringClass.class)) {
            return RECURRING_CLASSES;
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
