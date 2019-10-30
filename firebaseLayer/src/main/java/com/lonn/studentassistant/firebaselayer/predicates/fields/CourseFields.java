package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.models.Course;

import java.math.BigInteger;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CourseFields<T> extends EntityFields<Course, T> {
    public static CourseFields<String> COURSE_NAME = new CourseFields<>("courseName");
    public static CourseFields<Integer> YEAR = new CourseFields<>("year");
    public static CourseFields<Integer> SEMESTER = new CourseFields<>("semester");
    public static CourseFields<Integer> PACK = new CourseFields<>("pack");

    @Getter
    private String fieldName;
}