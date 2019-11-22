package com.lonn.studentassistant.testingapplication.testUtils;


import com.lonn.studentassistant.firebaselayer.models.Course;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CourseUtils {
    private static Random random = new Random();

    public static Course getRandomCourse() {
        int courseNumber = random.nextInt(1000000);

        int year = random.nextInt(4);
        int semester = random.nextInt(2) + 1;
        int pack = random.nextInt(3) + 1;
        String courseName = "Course " + courseNumber;
        String description = courseName + " description";
        String website = courseName + " website";

        List<String> professors = new ArrayList<>();
        List<String> scheduleClasses = new LinkedList<>();

        for (int i = 0; i < random.nextInt(5); i++) {
            professors.add("Professor" + i);
            scheduleClasses.add("ScheduleClass" + i);
        }

        return new Course()
                .setCourseName(courseName)
                .setDescription(description)
                .setPack(pack)
                .setYear(year)
                .setSemester(semester)
                .setWebsite(website)
                .setProfessors(professors)
                .setScheduleClasses(scheduleClasses);
    }

    public static List<Course> getRandomCourseList(int listSize) {
        List<Course> result = new LinkedList<>();

        for (int i = 0; i < listSize; i++)
            result.add(getRandomCourse());

        return result;
    }
}
