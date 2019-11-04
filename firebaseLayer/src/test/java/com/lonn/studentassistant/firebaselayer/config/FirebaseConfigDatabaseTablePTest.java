package com.lonn.studentassistant.firebaselayer.config;

import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class FirebaseConfigDatabaseTablePTest {
    @Parameter
    public DatabaseTable databaseTable;

    @Parameter(value = 1)
    public String expectedTableName;

    @Mock
    private FirebaseConfig config;

    @Parameters
    public static Object[][] dataProvider() {
        Object[][] parameters = new Object[5][];

        parameters[0] = new Object[]{DatabaseTable.COURSES, "Courses"};
        parameters[1] = new Object[]{DatabaseTable.EXAMS, "Exams"};
        parameters[2] = new Object[]{DatabaseTable.GRADES, "Grades"};
        parameters[3] = new Object[]{DatabaseTable.OTHER_ACTIVITIES, "OtherActivities"};
        parameters[3] = new Object[]{DatabaseTable.PROFESSORS, "Professors"};
        parameters[3] = new Object[]{DatabaseTable.SCHEDULE_CLASSES, "ScheduleClasses"};
        parameters[3] = new Object[]{DatabaseTable.STUDENTS, "Students"};
        parameters[3] = new Object[]{DatabaseTable.USERS, "Users"};
        parameters[4] = new Object[]{null, "UnknownTable"};

        return parameters;
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getTableName_shouldReturnCorrectTableName() {
        Mockito.when(config.getTableName(databaseTable)).thenCallRealMethod();

        assertEquals(expectedTableName,
                config.getTableName(databaseTable));
    }

    @Test
    public void getTableReference_shouldCallGetDatabaseName() {
        Mockito.when(config.getTableReference(databaseTable)).thenCallRealMethod();

        config.getTableReference(databaseTable);

        verify(config, times(1)).getDatabaseName();
    }

    @Test
    public void getTableReference_shouldCallGetTableName() {
        Mockito.when(config.getTableReference(databaseTable)).thenCallRealMethod();

        config.getTableReference(databaseTable);

        verify(config, times(1)).getTableName(databaseTable);
    }
}
