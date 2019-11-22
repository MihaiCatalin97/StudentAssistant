package com.lonn.studentassistant.firebaselayer.config;

import android.content.Context;
import android.test.mock.MockContext;
import android.test.mock.MockResources;

import com.lonn.studentassistant.firebaselayer.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FirebaseConfigGetDatabaseNamePTest {
    @Parameter
    public Context mockContext;

    @Parameter(value = 1)
    public String expectedDatabaseName;

    @Mock
    private FirebaseConfig config;

    @Parameters
    public static Object[][] dataProvider() {
        Object[][] parameters = new Object[3][];

        String getStringResult = UUID.randomUUID().toString();
        Context mockedContext = Mockito.mock(MockContext.class);

        MockResources mockResources = Mockito.mock(MockResources.class);
        Mockito.when(mockResources.getString(R.string.database_name)).thenReturn(getStringResult);
        Mockito.when(mockedContext.getResources()).thenReturn(mockResources);

        parameters[0] = new Object[]{null, "unknown"};
        parameters[1] = new Object[]{Mockito.mock(MockContext.class), "unknown"};
        parameters[2] = new Object[]{mockedContext, getStringResult};

        return parameters;
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(config.getContext()).thenReturn(mockContext);
        Mockito.when(config.getDatabaseName()).thenCallRealMethod();
    }

    @Test
    public void getDatabaseName_shouldReturnTheCorrectString() {
        String result = config.getDatabaseName();

        assertEquals(expectedDatabaseName, result);
    }
}
