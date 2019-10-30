package com.lonn.studentassistant.firebaselayer.requests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class DatabaseTablePTest {
    @Parameter
    public DatabaseTable databaseTable;

    @Parameter(value = 1)
    public boolean isGlobalTable;

    @Parameter(value = 2)
    public boolean isUserSpecificTable;


    @Parameters
    public static Object[][] dataProvider() {
        return new Object[][]{
                {DatabaseTable.COURSES, true, false},
                {DatabaseTable.EXAMS, true, false},
                {DatabaseTable.GRADES, true, false},
                {DatabaseTable.OTHER_ACTIVITIES, true, false},
                {DatabaseTable.PROFESSORS, true, false},
                {DatabaseTable.SCHEDULE_CLASSES, true, false},
                {DatabaseTable.STUDENTS, true, false},
                {DatabaseTable.USERS, true, false}
        };
    }

    @Test
    public void isGlobalTable_shouldReturnCorrectValue() {
        assertEquals(isGlobalTable, databaseTable.isGlobalTable());
    }

    @Test
    public void isUserSpecificTable_shouldReturnCorrectValue() {
        assertEquals(isUserSpecificTable, databaseTable.isUserSpecificTable());
    }
}
