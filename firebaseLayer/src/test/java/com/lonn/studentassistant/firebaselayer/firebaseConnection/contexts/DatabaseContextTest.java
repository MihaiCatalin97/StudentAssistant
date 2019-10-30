package com.lonn.studentassistant.firebaselayer.firebaseConnection.contexts;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.contexts.DatabaseContext.CustomValueEventListener;
import com.lonn.studentassistant.firebaselayer.interfaces.OnErrorCallback;
import com.lonn.studentassistant.firebaselayer.interfaces.OnSuccessCallback;
import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.hamcrest.MockitoHamcrest;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DatabaseContextTest {
    @Mock
    private DatabaseContext<Course> context;

    @Mock
    private Runnable onSuccess;

    @Mock
    private OnSuccessCallback onSuccessCallback;

    @Mock
    private OnErrorCallback onErrorCallback;

    @Mock
    private DatabaseReference database;

    @Mock
    private DatabaseReference childReference;

    @Mock
    private Task<Void> dbTask;

    @Mock
    private Predicate predicate;

    private UUID testUuid = UUID.randomUUID();

    @Mock
    private Course testCourse = new Course();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(context.getModelClass())
                .thenReturn(Course.class);

        when(childReference.setValue(testCourse))
                .thenReturn(dbTask);

        when(database.child(testCourse.computeKey()))
                .thenReturn(childReference);

        when(context.getDatabase())
                .thenReturn(database);

        doCallRealMethod()
                .when(context)
                .saveOrUpdate(any(Course.class),
                        any(Runnable.class),
                        any(OnErrorCallback.class));

        when(childReference.removeValue())
                .thenReturn(dbTask);

        when(database.child(testUuid.toString()))
                .thenReturn(childReference);

        when(context.getDatabase())
                .thenReturn(database);

        doCallRealMethod()
                .when(context)
                .delete(any(UUID.class),
                        any(Runnable.class),
                        any(OnErrorCallback.class));

        when(database.removeValue())
                .thenReturn(dbTask);

        doCallRealMethod()
                .when(context)
                .deleteAll(any(Runnable.class),
                        any(OnErrorCallback.class));

        doCallRealMethod()
                .when(context)
                .get(any(OnSuccessCallback.class),
                        any(OnErrorCallback.class));

        doCallRealMethod()
                .when(context)
                .get(any(OnSuccessCallback.class),
                        any(OnErrorCallback.class),
                        any(Predicate.class));

        doCallRealMethod()
                .when(context)
                .get(any(OnSuccessCallback.class),
                        any(OnErrorCallback.class),
                        nullable(Predicate.class));
    }

    @Test
    public void saveOrUpdate_shouldCallChildWithParameterId_whenDatabaseIsNotNull() {
        context.saveOrUpdate(testCourse, onSuccess, onErrorCallback);

        verify(database, times(1))
                .child(testCourse.computeKey());
    }

    @Test
    public void saveOrUpdate_shouldCallSetValueOnChild_whenDatabaseIsNotNull() {
        context.saveOrUpdate(testCourse, onSuccess, onErrorCallback);

        verify(childReference, times(1))
                .setValue(testCourse);
    }

    @Test
    public void saveOrUpdate_shouldCallAddListenersToTask_whenDatabaseIsNotNull() {
        context.saveOrUpdate(testCourse, onSuccess, onErrorCallback);

        verify(context, times(1))
                .addListenersToTask(dbTask, onSuccess, onErrorCallback);
    }

    @Test
    public void saveOrUpdate_shouldDoNothing_whenDatabaseIsNull() {
        when(context.getDatabase())

                .thenReturn(null);
        context.saveOrUpdate(testCourse, onSuccess, onErrorCallback);

        verify(database, times(0))
                .child(testCourse.computeKey());
        verify(childReference, times(0))
                .setValue(testCourse);
        verify(context, times(0))
                .addListenersToTask(dbTask, onSuccess, onErrorCallback);
    }

    @Test
    public void delete_shouldCallChildWithParameterUuid_whenDatabaseIsNotNull() {
        context.delete(testUuid, onSuccess, onErrorCallback);

        verify(database, times(1))
                .child(testUuid.toString());
    }

    @Test
    public void delete_shouldCallRemoveValueOnChild_whenDatabaseIsNotNull() {
        context.delete(testUuid, onSuccess, onErrorCallback);

        verify(childReference, times(1))
                .removeValue();
    }

    @Test
    public void delete_shouldCallAddListenersToTask_whenDatabaseIsNotNull() {
        context.delete(testUuid, onSuccess, onErrorCallback);

        verify(context, times(1))
                .addListenersToTask(dbTask, onSuccess, onErrorCallback);
    }

    @Test
    public void delete_shouldDoNothing_whenDatabaseIsNull() {
        when(context.getDatabase())

                .thenReturn(null);
        context.delete(testUuid, onSuccess, onErrorCallback);

        verify(database, times(0))
                .child(testUuid.toString());
        verify(childReference, times(0))
                .removeValue();
        verify(context, times(0))
                .addListenersToTask(dbTask, onSuccess, onErrorCallback);
    }

    @Test
    public void deleteAll_shouldCallRemoveValueOnDatabase_whenDatabaseIsNotNull() {
        context.deleteAll(onSuccess, onErrorCallback);

        verify(database, times(1))
                .removeValue();
    }

    @Test
    public void deleteAll_shouldCallAddListenersToTask_whenDatabaseIsNotNull() {
        context.deleteAll(onSuccess, onErrorCallback);

        verify(context, times(1))
                .addListenersToTask(dbTask, onSuccess, onErrorCallback);
    }

    @Test
    public void deleteAll_shouldDoNothing_whenDatabaseIsNull() {
        when(context.getDatabase())
                .thenReturn(null);

        context.deleteAll(onSuccess, onErrorCallback);

        verify(database, times(0))
                .removeValue();
        verify(context, times(0))
                .addListenersToTask(dbTask, onSuccess, onErrorCallback);
    }

    @Test
    public void getWithNoPredicate_shouldCallGetWithNullPredicate() {
        doNothing()
                .when(context)
                .get(onSuccessCallback, onErrorCallback, null);

        context.get(onSuccessCallback, onErrorCallback);

        verify(context, times(1))
                .get(onSuccessCallback, onErrorCallback, null);
    }

    @Test
    public void getWithWithPredicate_shouldDoNothing_whenDatabaseIsNull() {
        when(context.getDatabase())
                .thenReturn(null);

        context.get(onSuccessCallback, onErrorCallback, predicate);

        verify(database, times(0))
                .addValueEventListener(any(ValueEventListener.class));
        verify(predicate, times(0))
                .apply(any(DatabaseReference.class));
    }

    @Test
    public void getWithWithPredicate_shouldCallAddValueEventListenerOnDatabase_whenPredicateIsNull() {
        when(context.getDatabase())
                .thenReturn(database);

        context.get(onSuccessCallback, onErrorCallback, null);

        verify(database, times(1))
                .addValueEventListener(any(ValueEventListener.class));
        verify(predicate, times(0))
                .apply(any(DatabaseReference.class));
    }

    @Test
    public void getWithWithPredicate_shouldCallAddValueEventListenerOnPredicateApplyOnDatabase_whenPredicateIsNotNull() {
        Query predicateResultQuery = mock(Query.class);

        when(context.getDatabase())
                .thenReturn(database);
        when(predicate.apply(any(DatabaseReference.class)))
                .thenReturn(predicateResultQuery);

        context.get(onSuccessCallback, onErrorCallback, predicate);

        verify(predicate, times(1))
                .apply(database);
        verify(predicateResultQuery, times(1))
                .addValueEventListener(
                        any(DatabaseContext.CustomValueEventListener.class));
    }

    @Test
    public void CustomValueEventListener$OnDataChange_shouldDoNothing_whenOnSuccessIsNull() {
        CustomValueEventListener customValueEventListener =
                context.new CustomValueEventListener(null, onErrorCallback);
        DataSnapshot dataSnapshot = mock(DataSnapshot.class);

        customValueEventListener.onDataChange(dataSnapshot);

        verify(dataSnapshot, times(0))
                .getChildren();
    }

    @Test
    public void CustomValueEventListener$OnDataChange_callsOnSuccessCallbackWithListOfDataSnapshotChildren_whenOnSuccessIsNot() {
        CustomValueEventListener customValueEventListener =
                context.new CustomValueEventListener(onSuccessCallback, onErrorCallback);
        DataSnapshot dataSnapshot = mock(DataSnapshot.class);

        List<DataSnapshot> childrenSnapshot = new LinkedList<>();
        for (int i = 0; i < 10; i++)
            childrenSnapshot.add(mock(DataSnapshot.class));
        when(dataSnapshot.getChildren())
                .thenReturn(childrenSnapshot);

        List<BaseEntity> childEntities = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Course childEntity = mock(Course.class);
            childEntities.add(childEntity);

            when(childrenSnapshot.get(i).getValue(Course.class))
                    .thenReturn(childEntity);
        }


        customValueEventListener.onDataChange(dataSnapshot);

        verify(dataSnapshot, times(1))
                .getChildren();

        for (DataSnapshot child : childrenSnapshot) {
            verify(child, times(1))
                    .getValue(Course.class);

        }

        verify(onSuccessCallback, times(1))
                .callback(MockitoHamcrest.argThat(
                        Matchers.containsInAnyOrder(
                                childEntities.toArray())));
    }

    @Test
    public void CustomValueEventListener$OnCancelled_shouldDoNothing_whenOnErrorIsNull() {
        CustomValueEventListener customValueEventListener =
                context.new CustomValueEventListener(onSuccessCallback, null);
        DatabaseError databaseError = mock(DatabaseError.class);

        customValueEventListener.onCancelled(databaseError);

        verify(onErrorCallback, times(0))
                .callback(any(DatabaseError.class));
    }

    @Test
    public void CustomValueEventListener$OnCancelled_shouldCallOnError_whenOnErrorIsNotNull() {
        CustomValueEventListener customValueEventListener =
                context.new CustomValueEventListener(onSuccessCallback, onErrorCallback);
        DatabaseError databaseError = mock(DatabaseError.class);

        customValueEventListener.onCancelled(databaseError);

        verify(onErrorCallback, times(1))
                .callback(databaseError);
    }

    @Test
    public void constructor_shouldSetDatabaseToNull() {
        DatabaseContext<Course> courseContext = new DatabaseContext<>(null, Course.class);

        assertNull(courseContext.getDatabase());
    }
}
