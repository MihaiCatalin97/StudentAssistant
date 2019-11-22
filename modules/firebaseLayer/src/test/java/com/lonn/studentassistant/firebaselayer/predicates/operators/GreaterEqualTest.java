package com.lonn.studentassistant.firebaselayer.predicates.operators;

import com.google.firebase.database.DatabaseReference;
import com.lonn.studentassistant.firebaselayer.Utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GreaterEqualTest {
    @Mock
    private DatabaseReference databaseReference;
    private Random random = new Random();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(databaseReference.startAt(Mockito.any(Double.class))).thenReturn(databaseReference);
        when(databaseReference.endAt(Mockito.any(Double.class))).thenReturn(databaseReference);

        when(databaseReference.startAt(Mockito.any(Boolean.class))).thenReturn(databaseReference);
        when(databaseReference.endAt(Mockito.any(Boolean.class))).thenReturn(databaseReference);

        when(databaseReference.startAt(Mockito.any(String.class))).thenReturn(databaseReference);
        when(databaseReference.endAt(Mockito.any(String.class))).thenReturn(databaseReference);
    }

    @Test
    public void apply_shouldReturnAFunctionThatCallsStartAtAndEndAtOnQueryWithParameter_whenParameterIsInteger() {
        int value = random.nextInt();
        GreaterEqual<Number> testingOperator = new GreaterEqual<>();

        testingOperator.apply(value).apply(databaseReference);

        verify(databaseReference, times(1))
                .startAt(value);
        verify(databaseReference, times(0))
                .endAt(value);
    }

    @Test
    public void apply_shouldReturnAFunctionThatCallsStartAtAndEndAtOnQueryWithParameter_whenParameterIsDouble() {
        double value = random.nextDouble();
        GreaterEqual<Number> testingOperator = new GreaterEqual<>();

        testingOperator.apply(value).apply(databaseReference);

        verify(databaseReference, times(1))
                .startAt(value);
        verify(databaseReference, times(0))
                .endAt(value);
    }

    @Test
    public void apply_shouldReturnAFunctionThatCallsStartAtAndEndAtOnQueryWithParameter_whenParameterIsLong() {
        long value = random.nextLong();
        GreaterEqual<Number> testingOperator = new GreaterEqual<>();

        testingOperator.apply(value).apply(databaseReference);

        verify(databaseReference, times(1))
                .startAt(value);
        verify(databaseReference, times(0))
                .endAt(value);
    }

    @Test
    public void apply_shouldReturnAFunctionThatCallsStartAtAndEndAtOnQueryWithParameter_whenParameterIsBoolean() {
        boolean value = random.nextBoolean();
        GreaterEqual<Boolean> testingOperator = new GreaterEqual<>();

        testingOperator.apply(value).apply(databaseReference);

        verify(databaseReference, times(1))
                .startAt(value);
        verify(databaseReference, times(0))
                .endAt(value);
    }

    @Test
    public void apply_shouldReturnAFunctionThatCallsStartAtAndEndAtOnQueryWithParameterToString_whenParameterIsUUID() {
        UUID value = UUID.randomUUID();
        GreaterEqual<UUID> testingOperator = new GreaterEqual<>();

        testingOperator.apply(value).apply(databaseReference);

        verify(databaseReference, times(1))
                .startAt(value.toString());
        verify(databaseReference, times(0))
                .endAt(value.toString());
    }

    @Test
    public void apply_shouldReturnAFunctionThatCallsStartAtAndEndAtOnQueryWithParameter_whenParameterIsString() {
        String value = UUID.randomUUID().toString();
        GreaterEqual<String> testingOperator = new GreaterEqual<>();

        testingOperator.apply(value).apply(databaseReference);

        verify(databaseReference, times(1))
                .startAt(value);
        verify(databaseReference, times(0))
                .endAt(value);
    }

    @Test
    public void apply_shouldReturnAFunctionThatCallsStartAtAndEndAtOnQueryWithParameterToString_whenParameterIsBigInteger() {
        BigInteger value = new BigInteger(Integer.toString(random.nextInt()));
        GreaterEqual<BigInteger> testingOperator = new GreaterEqual<>();

        testingOperator.apply(value).apply(databaseReference);

        verify(databaseReference, times(1))
                .startAt(Utils.padWithZeroesToSize(value.toString()));
        verify(databaseReference, times(0))
                .endAt(Utils.padWithZeroesToSize(value.toString()));
    }
}
