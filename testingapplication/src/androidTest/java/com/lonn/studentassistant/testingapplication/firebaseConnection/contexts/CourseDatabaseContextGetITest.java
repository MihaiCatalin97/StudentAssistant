package com.lonn.studentassistant.testingapplication.firebaseConnection.contexts;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.lonn.studentassistant.firebaselayer.config.FirebaseConfig;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.contexts.DatabaseContext;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;
import com.lonn.studentassistant.firebaselayer.predicates.fields.CourseFields;
import com.lonn.studentassistant.firebaselayer.requests.DatabaseTable;
import com.lonn.studentassistant.testingapplication.TestingActivity;
import com.lonn.studentassistant.testingapplication.testUtils.CourseUtils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CourseDatabaseContextGetITest {
    private static List<CompletableFuture<Boolean>> completableFutures;
    private static List<Course> courseList;
    private static DatabaseContext<Course> courseContext;
    private static int listSize = 10;
    @Rule
    public ActivityTestRule<TestingActivity> activityRule = new ActivityTestRule<>(TestingActivity.class);
    private CompletableFuture<List<Course>> databaseResultFuture;

    @BeforeClass
    public static void staticStartUp() {
        courseContext = new DatabaseContext<>(
                new FirebaseConfig(InstrumentationRegistry.getInstrumentation()
                        .getTargetContext()
                        .getApplicationContext()).getTableReference(DatabaseTable.COURSES), Course.class);

        completableFutures = new LinkedList<>();
        courseList = CourseUtils.getRandomCourseList(listSize);

        courseList.forEach(Course -> {
            CompletableFuture<Boolean> saveFuture = new CompletableFuture<>();
            completableFutures.add(saveFuture);

            courseContext.saveOrUpdate(Course,
                    () -> saveFuture.complete(true),
                    (error) -> fail(error.getMessage()));
        });

        completableFutures.forEach(future -> {
            try {
                assertTrue("Error saving Coursees", future.get());
            } catch (InterruptedException | ExecutionException e) {
                fail("An error occurred while getting future results of the SAVE call");
            }
        });
    }

    @AfterClass
    public static void cleanUp() {
        courseContext.deleteAll(null, null);
    }

    @Before
    public void startUp() {
        databaseResultFuture = new CompletableFuture<>();
    }

    @Test
    public void get_shouldReturnAllEntities_whenNoPredicatesAreGiven() {
        courseContext.get(databaseResultFuture::complete,
                (error) -> fail(error.getMessage()));

        try {
            List<Course> actual = databaseResultFuture.get();
            List<Course> expected = courseList;

            assertTrue("Not all Course were found", actual.containsAll(expected));
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the GET call");
        }
    }

    @Test
    public void get_shouldReturnCorrectEntities_whenPredicateIsEqualAndFieldTypeIsInteger() {
        Integer filterValue = courseList.get(listSize / 2).getPack();

        courseContext.get(databaseResultFuture::complete,
                (error) -> fail(error.getMessage()),
                Predicate.where(CourseFields.PACK)
                        .equalTo(filterValue));

        try {
            List<Course> actual = databaseResultFuture.get();
            List<Course> expected = courseList.stream()
                    .filter(Course -> Course.getPack()
                            .equals(filterValue))
                    .collect(Collectors.toList());

            assertTrue("Not all Course were found",
                    actual.containsAll(expected));
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the GET call");
        }
    }

    @Test
    public void get_shouldReturnCorrectEntities_whenPredicateIsLessEqualAndFieldTypeIsInteger() {
        Integer filterValue = courseList.get(listSize / 2).getPack();

        courseContext.get(databaseResultFuture::complete,
                (error) -> fail(error.getMessage()),
                Predicate.where(CourseFields.PACK)
                        .lessEqual(filterValue));

        try {
            List<Course> actual = databaseResultFuture.get();
            List<Course> expected = courseList.stream()
                    .filter(Course -> Course.getPack()
                            .compareTo(filterValue) <= 0)
                    .collect(Collectors.toList());

            assertTrue("Not all Course were found",
                    actual.containsAll(expected));
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the GET call");
        }
    }

    @Test
    public void get_shouldReturnCorrectEntities_whenPredicateIsGreaterEqualAndFieldTypeIsInteger() {
        Integer filterValue = courseList.get(listSize / 2).getPack();

        courseContext.get(databaseResultFuture::complete,
                (error) -> fail(error.getMessage()),
                Predicate.where(CourseFields.PACK)
                        .greaterEqual(filterValue));

        try {
            List<Course> actual = databaseResultFuture.get();
            List<Course> expected = courseList.stream()
                    .filter(Course -> Course.getPack()
                            .compareTo(filterValue) >= 0)
                    .collect(Collectors.toList());

            assertTrue("Not all Course were found",
                    actual.containsAll(expected));
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the GET call");
        }
    }

    @Test
    public void get_shouldReturnCorrectEntities_whenPredicateIsEqualAndFieldTypeIsString() {
        String filterValue = courseList.get(listSize / 2).getCourseName();

        courseContext.get(databaseResultFuture::complete,
                (error) -> fail(error.getMessage()),
                Predicate.where(CourseFields.COURSE_NAME)
                        .equalTo(filterValue));

        try {
            List<Course> actual = databaseResultFuture.get();
            List<Course> expected = courseList.stream()
                    .filter(Course -> Course.getCourseName()
                            .equals(filterValue))
                    .collect(Collectors.toList());

            assertTrue("Not all Course were found",
                    actual.containsAll(expected));
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the GET call");
        }
    }

    @Test
    public void get_shouldReturnCorrectEntities_whenPredicateIsLessEqualAndFieldTypeIsString() {
        String filterValue = courseList.get(listSize / 2).getCourseName();

        courseContext.get(databaseResultFuture::complete,
                (error) -> fail(error.getMessage()),
                Predicate.where(CourseFields.COURSE_NAME)
                        .lessEqual(filterValue));

        try {
            List<Course> actual = databaseResultFuture.get();
            List<Course> expected = courseList.stream()
                    .filter(Course -> Course.getCourseName()
                            .compareTo(filterValue) <= 0)
                    .collect(Collectors.toList());

            assertTrue("Not all Course were found",
                    actual.containsAll(expected));
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the GET call");
        }
    }

    @Test
    public void get_shouldReturnCorrectEntities_whenPredicateIsGreaterEqualAndFieldTypeIsString() {
        String filterValue = courseList.get(listSize / 2).getCourseName();

        courseContext.get(databaseResultFuture::complete,
                (error) -> fail(error.getMessage()),
                Predicate.where(CourseFields.COURSE_NAME)
                        .greaterEqual(filterValue));

        try {
            List<Course> actual = databaseResultFuture.get();
            List<Course> expected = courseList.stream()
                    .filter(Course -> Course.getCourseName()
                            .compareTo(filterValue) >= 0)
                    .collect(Collectors.toList());

            assertTrue("Not all Course were found",
                    actual.containsAll(expected));
        } catch (InterruptedException | ExecutionException e) {
            fail("An error occurred while getting future results of the GET call");
        }
    }
}
