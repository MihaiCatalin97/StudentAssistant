package com.lonn.studentassistant.firebaselayer.predicates;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.predicates.fields.CourseFields;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IntermediaryPredicateTest {
    @Mock
    private CourseFields<Object> entityField;

    @Mock
    private Object comparingValue;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void equal_shouldReturnNewPredicateWithFieldAndEqualOperator() {
        IntermediaryPredicate<Course, Object> intermediaryPredicate = Predicate.where(entityField);
        Predicate<Course> result = intermediaryPredicate.equalTo(comparingValue);

        assertEquals(entityField, result.getField());
        assertNotNull(result.getOperatorFilter());
    }

    @Test
    public void lessEqual_shouldReturnNewPredicateWithFieldAndEqualOperator() {
        IntermediaryPredicate<Course, Object> intermediaryPredicate = Predicate.where(entityField);
        Predicate<Course> result = intermediaryPredicate.lessEqual(comparingValue);

        assertEquals(entityField, result.getField());
        assertNotNull(result.getOperatorFilter());
    }

    @Test
    public void greaterEqual_shouldReturnNewPredicateWithFieldAndEqualOperator() {
        IntermediaryPredicate<Course, Object> intermediaryPredicate = Predicate.where(entityField);
        Predicate<Course> result = intermediaryPredicate.greaterEqual(comparingValue);

        assertEquals(entityField, result.getField());
        assertNotNull(result.getOperatorFilter());
    }

}
