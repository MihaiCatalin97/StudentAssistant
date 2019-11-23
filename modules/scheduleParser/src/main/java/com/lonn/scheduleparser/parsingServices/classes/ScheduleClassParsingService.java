package com.lonn.scheduleparser.parsingServices.classes;

import android.util.Log;

import com.lonn.scheduleparser.parsingServices.abstractions.ParsingService;
import com.lonn.scheduleparser.parsingServices.courses.CourseParsingService;
import com.lonn.scheduleparser.parsingServices.otherActivities.OtherActivityParsingService;
import com.lonn.scheduleparser.parsingServices.professors.ProfessorParsingService;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.Professor;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class ScheduleClassParsingService extends ParsingService<ScheduleClass> {
    private static ScheduleClassParsingService instance;
    private static ProfessorParsingService professorParsingService;
    private static CourseParsingService courseParsingService;
    private static OtherActivityParsingService otherActivityParsingService;

    static {
        professorParsingService = ProfessorParsingService.getInstance();
        courseParsingService = CourseParsingService.getInstance();
        otherActivityParsingService = OtherActivityParsingService.getInstance();
    }

    private ScheduleClassParsingService() {
        repository = ScheduleClassRepository.getInstance();
        parser = new ScheduleClassParser();
    }

    public static ScheduleClassParsingService getInstance() {
        if (instance == null) {
            instance = new ScheduleClassParsingService();
        }
        return instance;
    }

    protected Future<List<ScheduleClass>> parse() {
        return newSingleThreadExecutor().submit(() -> {
            List<Course> allCourses = courseParsingService.getAll();
            List<OtherActivity> allOtherActivities = otherActivityParsingService.getAll();

            List<ScheduleClass> totalScheduleClasses = parseCoursesScheduleClasses(allCourses);
            totalScheduleClasses.addAll(parseOtherActivitiesScheduleClasses(allOtherActivities));

            repository.addAll(totalScheduleClasses);
            return repository.getAll();
        });
    }

    private List<ScheduleClass> parseCoursesScheduleClasses(List<Course> courses) {
        List<ScheduleClass> parsedEntities = new LinkedList<>();

        for (Course course : courses) {
            List<ScheduleClass> newEntities = parseSinglePage(course.getScheduleLink());

            if (newEntities != null) {
                for (ScheduleClass parsedEntity : newEntities) {
                    linkScheduleClassToCourse(course, parsedEntity);
                    linkScheduleClassToProfessors(parsedEntity);
                }
                parsedEntities.addAll(newEntities);
            }
        }

        return parsedEntities;
    }

    private List<ScheduleClass> parseOtherActivitiesScheduleClasses(List<OtherActivity> otherActivities) {
        List<ScheduleClass> parsedEntities = new LinkedList<>();

        for (OtherActivity otherActivity : otherActivities) {
            List<ScheduleClass> newEntities = parseSinglePage(otherActivity.getScheduleLink());

            if (newEntities != null) {
                for (ScheduleClass parsedEntity : newEntities) {
                    linkScheduleClassToOtherActivity(otherActivity, parsedEntity);
                    linkScheduleClassToProfessors(parsedEntity);
                }
                parsedEntities.addAll(newEntities);
            }
        }

        return parsedEntities;
    }

    private void linkScheduleClassToCourse(Course course, ScheduleClass scheduleClass) {
        scheduleClass.setCourse(course.getKey());
        course.getScheduleClasses().add(scheduleClass.getKey());
    }

    private void linkScheduleClassToOtherActivity(OtherActivity otherActivity, ScheduleClass scheduleClass) {
        scheduleClass.setCourse(otherActivity.getKey());
        otherActivity.getScheduleClasses().add(scheduleClass.getKey());
    }

    private void linkScheduleClassToProfessors(ScheduleClass scheduleClass) {
        List<String> professorScheduleLinks = new LinkedList<>(scheduleClass.getProfessors());

        scheduleClass.getProfessors().clear();

        for (String professorScheduleLink : professorScheduleLinks) {
            Professor professor =
                    professorParsingService.getByScheduleLink(professorScheduleLink);

            if (professor != null) {
                professor.getScheduleClasses().add(scheduleClass.getKey());
                scheduleClass.getProfessors().add(professor.getKey());
            }
            else {
                Log.e("Error finding professor",
                        "Professor schedule link: " + professorScheduleLink);
            }
        }
    }
}
