package com.lonn.studentassistant.firebaselayer.database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.firebaselayer.database.contexts.DatabaseContext;
import com.lonn.studentassistant.firebaselayer.database.contexts.DatabaseContext.CustomValueEventListener;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.interfaces.Consumer;
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
	private Consumer Consumer;

	@Mock
	private Consumer consumer;

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

		when(database.child(testCourse.getKey()))
				.thenReturn(childReference);

		when(context.getDatabase())
				.thenReturn(database);

		doCallRealMethod()
				.when(context)
				.saveOrUpdate(any(Course.class),
						any(Runnable.class),
						any(Consumer.class));

		when(childReference.removeValue())
				.thenReturn(dbTask);

		when(database.child(testUuid.toString()))
				.thenReturn(childReference);

		when(context.getDatabase())
				.thenReturn(database);

		doCallRealMethod()
				.when(context)
				.delete(any(String.class),
						any(Runnable.class),
						any(Consumer.class));

		when(database.removeValue())
				.thenReturn(dbTask);

		doCallRealMethod()
				.when(context)
				.deleteAll(any(Runnable.class),
						any(Consumer.class));

		doCallRealMethod()
				.when(context)
				.get(any(Consumer.class),
						any(Consumer.class));

		doCallRealMethod()
				.when(context)
				.get(any(Consumer.class),
						any(Consumer.class),
						any(Predicate.class));

		doCallRealMethod()
				.when(context)
				.get(any(Consumer.class),
						any(Consumer.class),
						nullable(Predicate.class));
	}

	@Test
	public void saveOrUpdate_shouldCallChildWithParameterId_whenDatabaseIsNotNull() {
		context.saveOrUpdate(testCourse, onSuccess, consumer);

		verify(database, times(1))
				.child(testCourse.getKey());
	}

	@Test
	public void saveOrUpdate_shouldCallSetValueOnChild_whenDatabaseIsNotNull() {
		context.saveOrUpdate(testCourse, onSuccess, consumer);

		verify(childReference, times(1))
				.setValue(testCourse);
	}

	@Test
	public void saveOrUpdate_shouldCallAddListenersToTask_whenDatabaseIsNotNull() {
		context.saveOrUpdate(testCourse, onSuccess, consumer);

		verify(context, times(1))
				.addListenersToTask(dbTask, onSuccess, consumer);
	}

	@Test
	public void saveOrUpdate_shouldDoNothing_whenDatabaseIsNull() {
		when(context.getDatabase())

				.thenReturn(null);
		context.saveOrUpdate(testCourse, onSuccess, consumer);

		verify(database, times(0))
				.child(testCourse.getKey());
		verify(childReference, times(0))
				.setValue(testCourse);
		verify(context, times(0))
				.addListenersToTask(dbTask, onSuccess, consumer);
	}

	@Test
	public void delete_shouldCallChildWithParameterUuid_whenDatabaseIsNotNull() {
		context.delete(testUuid.toString(), onSuccess, consumer);

		verify(database, times(1))
				.child(testUuid.toString());
	}

	@Test
	public void delete_shouldCallRemoveValueOnChild_whenDatabaseIsNotNull() {
		context.delete(testUuid.toString(), onSuccess, consumer);

		verify(childReference, times(1))
				.removeValue();
	}

	@Test
	public void delete_shouldCallAddListenersToTask_whenDatabaseIsNotNull() {
		context.delete(testUuid.toString(), onSuccess, consumer);

		verify(context, times(1))
				.addListenersToTask(dbTask, onSuccess, consumer);
	}

	@Test
	public void delete_shouldDoNothing_whenDatabaseIsNull() {
		when(context.getDatabase())

				.thenReturn(null);
		context.delete(testUuid.toString(), onSuccess, consumer);

		verify(database, times(0))
				.child(testUuid.toString());
		verify(childReference, times(0))
				.removeValue();
		verify(context, times(0))
				.addListenersToTask(dbTask, onSuccess, consumer);
	}

	@Test
	public void deleteAll_shouldCallRemoveValueOnDatabase_whenDatabaseIsNotNull() {
		context.deleteAll(onSuccess, consumer);

		verify(database, times(1))
				.removeValue();
	}

	@Test
	public void deleteAll_shouldCallAddListenersToTask_whenDatabaseIsNotNull() {
		context.deleteAll(onSuccess, consumer);

		verify(context, times(1))
				.addListenersToTask(dbTask, onSuccess, consumer);
	}

	@Test
	public void deleteAll_shouldDoNothing_whenDatabaseIsNull() {
		when(context.getDatabase())
				.thenReturn(null);

		context.deleteAll(onSuccess, consumer);

		verify(database, times(0))
				.removeValue();
		verify(context, times(0))
				.addListenersToTask(dbTask, onSuccess, consumer);
	}

	@Test
	public void getWithNoPredicate_shouldCallGetWithNullPredicate() {
		doNothing()
				.when(context)
				.get(Consumer, consumer, null);

		context.get(Consumer, consumer);

		verify(context, times(1))
				.get(Consumer, consumer, null);
	}

	@Test
	public void getWithWithPredicate_shouldDoNothing_whenDatabaseIsNull() {
		when(context.getDatabase())
				.thenReturn(null);

		context.get(Consumer, consumer, predicate);

		verify(database, times(0))
				.addValueEventListener(any(ValueEventListener.class));
		verify(predicate, times(0))
				.apply(any(DatabaseReference.class));
	}

	@Test
	public void getWithWithPredicate_shouldCallAddValueEventListenerOnDatabase_whenPredicateIsNull() {
		when(context.getDatabase())
				.thenReturn(database);

		context.get(Consumer, consumer, null);

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

		context.get(Consumer, consumer, predicate);

		verify(predicate, times(1))
				.apply(database);
		verify(predicateResultQuery, times(1))
				.addValueEventListener(
						any(DatabaseContext.CustomValueEventListener.class));
	}

	@Test
	public void CustomValueEventListener$OnDataChange_shouldDoNothing_whenOnSuccessIsNull() {
		CustomValueEventListener customValueEventListener =
				context.new CustomValueEventListener(null, consumer);
		DataSnapshot dataSnapshot = mock(DataSnapshot.class);

		customValueEventListener.onDataChange(dataSnapshot);

		verify(dataSnapshot, times(0))
				.getChildren();
	}

	@Test
	public void CustomValueEventListener$OnDataChange_callsConsumerWithListOfDataSnapshotChildren_whenOnSuccessIsNot() {
		CustomValueEventListener customValueEventListener =
				context.new CustomValueEventListener(Consumer, consumer);
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

		verify(Consumer, times(1))
				.consume(MockitoHamcrest.argThat(
						Matchers.containsInAnyOrder(
								childEntities.toArray())));
	}

	@Test
	public void CustomValueEventListener$OnCancelled_shouldDoNothing_whenOnErrorIsNull() {
		CustomValueEventListener customValueEventListener =
				context.new CustomValueEventListener(Consumer, null);
		DatabaseError databaseError = mock(DatabaseError.class);

		customValueEventListener.onCancelled(databaseError);

		verify(consumer, times(0))
				.consume(any(DatabaseError.class));
	}

	@Test
	public void CustomValueEventListener$OnCancelled_shouldCallOnError_whenOnErrorIsNotNull() {
		CustomValueEventListener customValueEventListener =
				context.new CustomValueEventListener(Consumer, consumer);
		DatabaseError databaseError = mock(DatabaseError.class);

		customValueEventListener.onCancelled(databaseError);

		verify(consumer, times(1))
				.consume(databaseError);
	}

	@Test
	public void constructor_shouldSetDatabaseToNull() {
		DatabaseContext<Course> courseContext = new DatabaseContext<>(null, Course.class);

		assertNull(courseContext.getDatabase());
	}
}
