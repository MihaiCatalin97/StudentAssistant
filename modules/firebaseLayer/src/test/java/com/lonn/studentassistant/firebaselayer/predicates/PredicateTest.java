package com.lonn.studentassistant.firebaselayer.predicates;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.lonn.studentassistant.firebaselayer.predicates.fields.CourseFields;
import com.lonn.studentassistant.firebaselayer.predicates.operators.Operator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PredicateTest {
    @Mock
    private CourseFields<Object> entityField;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void where_shouldReturnNewIntermediaryPredicateWithField() {
        IntermediaryPredicate predicate = Predicate.where(entityField);

        assertEquals(entityField, predicate.getField());
    }

    @Test
    public void apply_shouldCallOperatorApplyOnDatabaseOrderByChild_whenFieldIsId() {
        CourseFields<String> filteringField = CourseFields.COURSE_NAME;
        Operator.OperatorFilter mockFilter = mock(Operator.OperatorFilter.class);
        Query mockQuery = mock(Query.class);
        Query mockResultQuery = mock(Query.class);

        DatabaseReference mockDatabaseReference = mock(DatabaseReference.class);
        when(mockDatabaseReference.orderByChild(filteringField.getFieldName())).thenReturn(mockQuery);
        when(mockFilter.apply(mockQuery)).thenReturn(mockResultQuery);

        Query result = new Predicate<>(filteringField, mockFilter).apply(mockDatabaseReference);

        assertEquals(mockResultQuery, result);
        verify(mockDatabaseReference, times(1)).orderByChild(filteringField.getFieldName());
        verify(mockFilter, times(1)).apply(mockQuery);
    }
}
