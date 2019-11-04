package com.lonn.studentassistant.firebaselayer.firebaseConnection;

import android.test.mock.MockContext;

import com.lonn.studentassistant.firebaselayer.database.contexts.DatabaseContext;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;

public class FirebaseConnectionTest {
    @Mock
    private MockContext context;

    private DatabaseContext courseDatabaseContext;
    private DatabaseContext examDatabaseContext;
    private DatabaseContext gradeDatabaseContext;
    private DatabaseContext otherActivityDatabaseContext;
    private DatabaseContext professorDatabaseContext;
    private DatabaseContext scheduleClassDatabaseContext;
    private DatabaseContext studentDatabaseContext;
    private DatabaseContext userDatabaseContext;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        FirebaseConnection firebaseConnection;
        firebaseConnection = FirebaseConnection.getInstance(context);

        courseDatabaseContext = firebaseConnection.getDatabaseMap().get(DatabaseTable.COURSES);
        examDatabaseContext = firebaseConnection.getDatabaseMap().get(DatabaseTable.EXAMS);
        gradeDatabaseContext = firebaseConnection.getDatabaseMap().get(DatabaseTable.GRADES);
        otherActivityDatabaseContext = firebaseConnection.getDatabaseMap().get(DatabaseTable.OTHER_ACTIVITIES);
        professorDatabaseContext = firebaseConnection.getDatabaseMap().get(DatabaseTable.PROFESSORS);
        scheduleClassDatabaseContext = firebaseConnection.getDatabaseMap().get(DatabaseTable.SCHEDULE_CLASSES);
        studentDatabaseContext = firebaseConnection.getDatabaseMap().get(DatabaseTable.STUDENTS);
        userDatabaseContext = firebaseConnection.getDatabaseMap().get(DatabaseTable.USERS);
    }

    @Test
    public void databaseMap_shouldContainAllContexts() {
        assertNotNull(courseDatabaseContext);
        assertNotNull(examDatabaseContext);
        assertNotNull(gradeDatabaseContext);
        assertNotNull(otherActivityDatabaseContext);
        assertNotNull(professorDatabaseContext);
        assertNotNull(scheduleClassDatabaseContext);
        assertNotNull(studentDatabaseContext);
        assertNotNull(userDatabaseContext);
    }
}
