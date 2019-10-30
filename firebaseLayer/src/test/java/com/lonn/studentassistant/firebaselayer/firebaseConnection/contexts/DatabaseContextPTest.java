package com.lonn.studentassistant.firebaselayer.firebaseConnection.contexts;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.lonn.studentassistant.firebaselayer.interfaces.OnErrorCallback;
import com.lonn.studentassistant.firebaselayer.models.Course;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class DatabaseContextPTest {
    @Parameter
    public Runnable onSuccess;
    @Parameter(value = 1)
    public OnErrorCallback<Exception> onError;
    @Parameter(value = 2)
    public int onSuccessInvocations;
    @Parameter(value = 3)
    public int onErrorInvocations;

    @Mock
    private DatabaseContext<Course> context;

    @Mock
    private Task<Void> dbTask;

    @Mock
    private Exception exception = Mockito.mock(Exception.class);

    @Parameters
    public static Object[][] dataProvider() {
        Object[][] parameters = new Object[4][];

        Runnable onSuccess = Mockito.mock(Runnable.class);
        OnErrorCallback onError = Mockito.mock(OnErrorCallback.class);

        parameters[0] = new Object[]{onSuccess, onError, 1, 1};
        parameters[1] = new Object[]{onSuccess, null, 1, 0};
        parameters[2] = new Object[]{null, onError, 0, 1};
        parameters[3] = new Object[]{null, null, 0, 0};

        return parameters;
    }

    @Before
    @SuppressWarnings("unchecked")
    public void init() {
        MockitoAnnotations.initMocks(this);
        Mockito.doCallRealMethod().when(context).addListenersToTask(dbTask, onSuccess, onError);

        Mockito.when(dbTask.addOnCompleteListener(Mockito.any(OnCompleteListener.class))).then((InvocationOnMock invocation) -> {
            onSuccess.run();
            return dbTask;
        });

        Mockito.when(dbTask.addOnFailureListener(Mockito.any(OnFailureListener.class))).then((InvocationOnMock invocation) -> {
            onError.callback(exception);
            return dbTask;
        });
    }

    @Test
    public void addListenersToTask_shouldCallAddOnCompleteListenerAndOnSuccess() {
        context.addListenersToTask(dbTask, onSuccess, onError);

        verify(dbTask, times(onSuccessInvocations)).addOnCompleteListener(Mockito.any());

        if (onSuccess != null) {
            verify(onSuccess, times(onSuccessInvocations)).run();
        }
    }

    @Test
    public void addListenersToTask_shouldCallAddOnFailureListenerAndOnError() {
        context.addListenersToTask(dbTask, onSuccess, onError);

        verify(dbTask, times(onErrorInvocations)).addOnFailureListener(Mockito.any());

        if (onError != null) {
            verify(onError, times(onErrorInvocations)).callback(exception);
        }
    }

    @After
    @SuppressWarnings("unchecked")
    public void tearDown() {
        if (onSuccess != null) {
            Mockito.reset(onSuccess);
        }

        if (onError != null) {
            Mockito.reset(onError);
        }
    }
}
