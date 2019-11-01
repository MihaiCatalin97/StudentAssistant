package com.lonn.studentassistant.firebaselayer;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static com.lonn.studentassistant.firebaselayer.Utils.generateHashDigest;

public class UtilsTest {
    @Test
    public void generateHashDigest_shouldReturnCorrectHash_whenArgumentIsNotNull(){
        String input = "testingHash";
        String expectedOutput = "ab4d6bf875c41ddb1b4740bc0db96191";

        assertThat("Hashes are not equal",
                generateHashDigest(input),
                equalTo(expectedOutput));
    }

    @Test
    public void generateHashDigest_shouldReturnNull_whenArgumentIsNull(){
        assertThat("Null not returned",
                generateHashDigest(null),
                equalTo(null));
    }
}
