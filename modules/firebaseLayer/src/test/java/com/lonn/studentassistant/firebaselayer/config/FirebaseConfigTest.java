package com.lonn.studentassistant.firebaselayer.config;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseConfigTest {
    @Mock
    private Context mockContext;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void context_shouldContainConstructorParameter() {
        FirebaseConfig config = new FirebaseConfig(mockContext);
        assertEquals(config.getContext(), mockContext);
    }
}
