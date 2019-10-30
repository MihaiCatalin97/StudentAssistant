package com.lonn.studentassistant.firebaselayer.firebaseConnection;

import com.lonn.studentassistant.firebaselayer.firebaseConnection.contexts.DatabaseContext;
import com.lonn.studentassistant.firebaselayer.interfaces.OnErrorCallback;
import com.lonn.studentassistant.firebaselayer.interfaces.OnSuccessCallback;
import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.Exam;
import com.lonn.studentassistant.firebaselayer.models.Grade;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.Professor;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.models.Student;
import com.lonn.studentassistant.firebaselayer.models.User;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;
import com.lonn.studentassistant.firebaselayer.requests.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.requests.DeleteRequest;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.times;

@RunWith(Parameterized.class)
public class FirebaseConnectionPTest {
    @Parameter
    public GetRequest<? extends BaseEntity> getRequest;

    @Parameter(value = 1)
    public SaveRequest<? extends BaseEntity> saveRequest;

    @Parameter(value = 2)
    public DeleteRequest deleteRequest;

    @Parameter(value = 3)
    public DatabaseTable requestDatabaseTable;

    @Mock
    private FirebaseConnection firebaseConnection;
    @Mock
    private DatabaseContext<Course> courseContext;
    @Mock
    private DatabaseContext<Exam> examContext;
    @Mock
    private DatabaseContext<Grade> gradeContext;
    @Mock
    private DatabaseContext<OtherActivity> otherActivityContext;
    @Mock
    private DatabaseContext<Professor> professorContext;
    @Mock
    private DatabaseContext<ScheduleClass> scheduleClassContext;
    @Mock
    private DatabaseContext<Student> studentContext;
    @Mock
    private DatabaseContext<User> userContext;
    @Mock
    private Map<DatabaseTable, DatabaseContext> databaseMap;

    @Parameters
    @SuppressWarnings("unchecked")
    public static Object[][] dataProvider() {
        Object[][] parameters = new Object[DatabaseTable.values().length][];

        for (int i = 0; i < DatabaseTable.values().length; i++) {
            DatabaseTable databaseTable = DatabaseTable.values()[i];

            OnErrorCallback onErrorCallback = Mockito.mock(OnErrorCallback.class);
            OnSuccessCallback onSuccessCallback = Mockito.mock(OnSuccessCallback.class);
            Predicate<Student> predicate = Mockito.mock(Predicate.class);

            GetRequest<Student> getRequest = new GetRequest<Student>()
                    .databaseTable(databaseTable)
                    .onError(onErrorCallback)
                    .onSuccess(onSuccessCallback)
                    .predicate(predicate);

            Runnable onSuccess = Mockito.mock(Runnable.class);
            BaseEntity savingEntity = Mockito.mock(databaseTable.getTableClass());

            SaveRequest<Student> saveRequest = new SaveRequest<>()
                    .databaseTable(databaseTable)
                    .onError(onErrorCallback)
                    .onSuccess(onSuccess)
                    .entity(savingEntity);

            DeleteRequest deleteRequest = new DeleteRequest()
                    .databaseTable(databaseTable)
                    .onError(onErrorCallback)
                    .onSuccess(onSuccess)
                    .id(UUID.randomUUID());

            parameters[i] = new Object[]{getRequest, saveRequest, deleteRequest, databaseTable};
        }

        return parameters;
    }

    @Before
    @SuppressWarnings("unchecked")
    public void init() {
        MockitoAnnotations.initMocks(this);

        Mockito.doCallRealMethod().when(firebaseConnection).execute(Mockito.any(DeleteRequest.class));
        Mockito.doCallRealMethod().when(firebaseConnection).execute(Mockito.any(GetRequest.class));
        Mockito.doCallRealMethod().when(firebaseConnection).execute(Mockito.any(SaveRequest.class));

        Mockito.when(firebaseConnection.getDatabaseMap()).thenReturn(databaseMap);
        Mockito.when(databaseMap.get(DatabaseTable.COURSES)).thenReturn(courseContext);
        Mockito.when(databaseMap.get(DatabaseTable.EXAMS)).thenReturn(examContext);
        Mockito.when(databaseMap.get(DatabaseTable.GRADES)).thenReturn(gradeContext);
        Mockito.when(databaseMap.get(DatabaseTable.OTHER_ACTIVITIES)).thenReturn(otherActivityContext);
        Mockito.when(databaseMap.get(DatabaseTable.PROFESSORS)).thenReturn(professorContext);
        Mockito.when(databaseMap.get(DatabaseTable.SCHEDULE_CLASSES)).thenReturn(scheduleClassContext);
        Mockito.when(databaseMap.get(DatabaseTable.STUDENTS)).thenReturn(studentContext);
        Mockito.when(databaseMap.get(DatabaseTable.USERS)).thenReturn(userContext);

        System.out.println(getRequest + " " + saveRequest + " " + deleteRequest);
    }

    @Test
    public void executeGetRequest_shouldCallGetWithAccordingTable() {
        firebaseConnection.execute(getRequest);

        Mockito.verify(databaseMap, times(1)).get(requestDatabaseTable);
    }

    @Test
    public void executeSaveRequest_shouldCallGetWithAccordingTable() {
        firebaseConnection.execute(saveRequest);

        Mockito.verify(databaseMap, times(1)).get(requestDatabaseTable);
    }

    @Test
    public void executeDeleteRequest_shouldCallGetWithAccordingTable() {
        firebaseConnection.execute(deleteRequest);

        Mockito.verify(databaseMap, times(1)).get(requestDatabaseTable);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void executeGetRequest_shouldCallGetOnContext() {
        firebaseConnection.execute(getRequest);

        Mockito.verify(databaseMap.get(requestDatabaseTable), times(1)).get(getRequest.onSuccess(), getRequest.onError(), getRequest.predicate());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void executeSaveRequest_shouldCallSaveOrUpdateOnContext() {
        firebaseConnection.execute(saveRequest);

        Mockito.verify(databaseMap.get(requestDatabaseTable), times(1)).saveOrUpdate(saveRequest.entity(), saveRequest.onSuccess(), saveRequest.onError());
    }

    @Test
    public void executeDeleteRequest_shouldCallDeleteOnContext() {
        firebaseConnection.execute(deleteRequest);

        Mockito.verify(databaseMap.get(requestDatabaseTable), times(1)).delete(deleteRequest.id(), deleteRequest.onSuccess(), deleteRequest.onError());
    }

    @Test
    public void executeGetRequest_shouldNotThrowException() {
        Mockito.when(databaseMap.get(getRequest.databaseTable())).thenReturn(null);
        firebaseConnection.execute(getRequest);
    }

    @Test
    public void executeSaveRequest_shouldNotThrowException() {
        Mockito.when(databaseMap.get(saveRequest.databaseTable())).thenReturn(null);
        firebaseConnection.execute(saveRequest);
    }

    @Test
    public void executeDeleteRequest_shouldNotThrowException() {
        Mockito.when(databaseMap.get(deleteRequest.databaseTable())).thenReturn(null);
        firebaseConnection.execute(deleteRequest);
    }
}
