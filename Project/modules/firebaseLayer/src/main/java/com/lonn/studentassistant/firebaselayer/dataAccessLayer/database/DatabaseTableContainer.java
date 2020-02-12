package com.lonn.studentassistant.firebaselayer.dataAccessLayer.database;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Announcement;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Grade;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Laboratory;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.RegistrationToken;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.User;

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
	public static final DatabaseTable<FileMetadata> FILE_METADATA;
	public static final DatabaseTable<FileContent> FILE_CONTENT;
	public static final DatabaseTable<Laboratory> LABORATORIES;
	public static final DatabaseTable<RegistrationToken> REGISTRATION_TOKENS;
	public static final DatabaseTable<Announcement> ANNOUNCEMENTS;

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
		FILE_METADATA = new DatabaseTable<>(FileMetadata.class, "Files/Metadata");
		FILE_CONTENT = new DatabaseTable<>(FileContent.class, "Files/Content");
		LABORATORIES = new DatabaseTable<>(Laboratory.class, "Laboratories");
		REGISTRATION_TOKENS = new DatabaseTable<>(RegistrationToken.class, "RegistrationTokens");
		ANNOUNCEMENTS = new DatabaseTable<>(Announcement.class, "Announcements");
	}
}
