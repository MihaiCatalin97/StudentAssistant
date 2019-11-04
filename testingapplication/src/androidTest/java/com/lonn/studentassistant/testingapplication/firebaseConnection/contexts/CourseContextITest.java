package com.lonn.studentassistant.testingapplication.firebaseConnection.contexts;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.lonn.studentassistant.firebaselayer.config.FirebaseConfig;
import com.lonn.studentassistant.firebaselayer.database.contexts.DatabaseContext;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.testingapplication.TestingActivity;
import com.lonn.studentassistant.testingapplication.testUtils.CourseUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CourseContextITest {
    @Rule
    public ActivityTestRule<TestingActivity> activityRule = new ActivityTestRule<>(TestingActivity.class);
    private DatabaseContext<Course> courseDatabaseContext;

    @Before
    public void init() {
        courseDatabaseContext = new DatabaseContext<>(
                new FirebaseConfig(activityRule.getActivity()
                        .getBaseContext()).getTableReference(DatabaseTable.COURSES), Course.class);
    }

    @Test
    public void saveOrUpdate_shouldCallOnSuccess() {
        Course testingCourse = CourseUtils.getRandomCourse();

        CompletableFuture<Boolean> databaseResultFuture = new CompletableFuture<>();

        courseDatabaseContext.saveOrUpdate(testingCourse,
                () -> databaseResultFuture.complete(true),
                (error) -> fail(error.getMessage()));

        try {
            assertTrue("Error saving course", databaseResultFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the SAVE call");
        }
    }

    @Test
    public void saveOrUpdate_shouldCreate_whenEntityDoesNotExist() {
        Course testingCourse = CourseUtils.getRandomCourse();

        CompletableFuture<Boolean> saveResult = new CompletableFuture<>();
        CompletableFuture<List<Course>> getResult = new CompletableFuture<>();

        courseDatabaseContext.saveOrUpdate(testingCourse,
                () -> saveResult.complete(true),
                (error) -> fail(error.getMessage()));

        try {
            assertTrue("Error saving course", saveResult.get());
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the SAVE call");
        }

        courseDatabaseContext.get(getResult::complete,
                (error) -> fail(error.getMessage()));

        try {
            assertTrue("Not all course were found", getResult.get().contains(testingCourse));
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the GET call");
        }
    }

    @Test
    public void saveOrUpdate_shouldUpdate_whenEntityExists() {
        Course testingCourse = CourseUtils.getRandomCourse();

        CompletableFuture<Boolean> saveResult = new CompletableFuture<>();
        CompletableFuture<List<Course>> getResult = new CompletableFuture<>();

        courseDatabaseContext.saveOrUpdate(testingCourse,
                () -> saveResult.complete(true),
                (error) -> fail(error.getMessage()));

        try {
            assertTrue("Error saving course", saveResult.get());
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the SAVE call");
        }

        testingCourse.setDescription("Test description");

        courseDatabaseContext.saveOrUpdate(testingCourse,
                () -> saveResult.complete(true),
                (error) -> fail(error.getMessage()));

        try {
            assertTrue("Error saving course", saveResult.get());
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the SAVE call");
        }

        courseDatabaseContext.get(getResult::complete,
                (error) -> fail(error.getMessage()));

        try {
            assertTrue("Not all course were found", getResult.get().contains(testingCourse));
            assertEquals("Created more than one course", 1, getResult.get().size());
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the GET call");
        }
    }

    @Test
    public void delete_shouldDeleteTheEntityCorrectly() {
        int listSize = 10;
        int deletingIndex = listSize / 2;

        List<CompletableFuture<Boolean>> saveFutures = new LinkedList<>();
        CompletableFuture<List<Course>> getFuture = new CompletableFuture<>();
        CompletableFuture<Boolean> deleteFuture = new CompletableFuture<>();
        List<Course> courseList = CourseUtils.getRandomCourseList(listSize);

        courseList.forEach(course -> {
            CompletableFuture<Boolean> saveFuture = new CompletableFuture<>();
            saveFutures.add(saveFuture);

            courseDatabaseContext.saveOrUpdate(course,
                    () -> saveFuture.complete(true),
                    (error) -> fail(error.getMessage()));
        });

        saveFutures.forEach(future -> {
            try {
                assertTrue("Error saving coursees", future.get());
            } catch (InterruptedException | ExecutionException e) {
                fail("An error occurred while getting future results of the SAVE call");
            }
        });

        courseDatabaseContext.delete(courseList.get(deletingIndex).getKey(),
                () -> deleteFuture.complete(true),
                (error) -> fail(error.getMessage()));

        try {
            assertTrue("Error deleting course", deleteFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the DELETE call");
        }

        courseDatabaseContext.get(getFuture::complete,
                (error) -> fail(error.getMessage()));

        try {
            List<Course> getResult = getFuture.get();
            Course deletedCourse = courseList.get(deletingIndex);
            courseList.remove(deletingIndex);

            assertFalse("Course was not deleted", getResult.contains(deletedCourse));
            assertTrue("Not all course were found", getFuture.get().containsAll(courseList));
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the GET call");
        }
    }

    @Test
    public void deleteAll_shouldDeleteAll() {
        int listSize = 10;

        List<CompletableFuture<Boolean>> saveFutures = new LinkedList<>();
        CompletableFuture<List<Course>> getFuture = new CompletableFuture<>();
        CompletableFuture<Boolean> deleteFuture = new CompletableFuture<>();
        List<Course> courseList = CourseUtils.getRandomCourseList(listSize);

        courseList.forEach(course -> {
            CompletableFuture<Boolean> saveFuture = new CompletableFuture<>();
            saveFutures.add(saveFuture);

            courseDatabaseContext.saveOrUpdate(course,
                    () -> saveFuture.complete(true),
                    (error) -> fail(error.getMessage()));
        });

        saveFutures.forEach(future -> {
            try {
                assertTrue("Error saving coursees", future.get());
            } catch (InterruptedException | ExecutionException e) {
                fail("An error occurred while getting future results of the SAVE call");
            }
        });

        courseDatabaseContext.deleteAll(() -> deleteFuture.complete(true),
                (error) -> fail(error.getMessage()));

        try {
            assertTrue("Error deleting course", deleteFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the DELETE call");
        }

        courseDatabaseContext.get(getFuture::complete,
                (error) -> fail(error.getMessage()));

        try {
            List<Course> getResult = getFuture.get();

            assertEquals("Course was not deleted", 0, getResult.size());
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the GET call");
        }
    }

    @After
    public void cleanUp() {
        courseDatabaseContext.deleteAll(null, null);
    }
}
