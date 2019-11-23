package com.lonn.scheduleparser.parsingServices.exams;

import android.util.Log;

import com.lonn.scheduleparser.parsingServices.abstractions.ParsingService;
import com.lonn.scheduleparser.parsingServices.courses.CourseParsingService;
import com.lonn.scheduleparser.parsingServices.otherActivities.OtherActivityParsingService;
import com.lonn.scheduleparser.parsingServices.professors.ProfessorParsingService;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.Professor;
import com.lonn.studentassistant.firebaselayer.models.Exam;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class ExamParsingService extends ParsingService<Exam> {
    private static ExamParsingService instance;
    private static ProfessorParsingService professorParsingService;
    private static CourseParsingService courseParsingService;
    private static OtherActivityParsingService otherActivityParsingService;

    static {
        professorParsingService = ProfessorParsingService.getInstance();
        courseParsingService = CourseParsingService.getInstance();
        otherActivityParsingService = OtherActivityParsingService.getInstance();
    }

    private ExamParsingService() {
        repository = ExamRepository.getInstance();
        parser = new ExamParser();
    }

    public static ExamParsingService getInstance() {
        if (instance == null) {
            instance = new ExamParsingService();
        }
        return instance;
    }

    protected Future<List<Exam>> parse() {
        return newSingleThreadExecutor().submit(() -> {
            List<Course> allCourses = courseParsingService.getAll();
            List<OtherActivity> allOtherActivities = otherActivityParsingService.getAll();

            List<Exam> totalExams = parseCoursesExams(allCourses);
            totalExams.addAll(parseOtherActivitiesExams(allOtherActivities));

            repository.addAll(totalExams);
            return repository.getAll();
        });
    }

    private List<Exam> parseCoursesExams(List<Course> courses) {
        List<Exam> parsedEntities = new LinkedList<>();

        for (Course course : courses) {
            List<Exam> newEntities = parseSinglePage(course.getScheduleLink());

            if (newEntities != null) {
                for (Exam parsedEntity : newEntities) {
                    linkExamToCourse(course, parsedEntity);
                    linkExamToProfessors(parsedEntity);
                }
                parsedEntities.addAll(newEntities);
            }
        }

        return parsedEntities;
    }

    private List<Exam> parseOtherActivitiesExams(List<OtherActivity> otherActivities) {
        List<Exam> parsedEntities = new LinkedList<>();

        for (OtherActivity otherActivity : otherActivities) {
            List<Exam> newEntities = parseSinglePage(otherActivity.getScheduleLink());

            if (newEntities != null) {
                for (Exam parsedEntity : newEntities) {
                    linkExamToOtherActivity(otherActivity, parsedEntity);
                    linkExamToProfessors(parsedEntity);
                }
                parsedEntities.addAll(newEntities);
            }
        }

        return parsedEntities;
    }

    private void linkExamToCourse(Course course, Exam exam) {
        exam.setCourse(course.getKey());
        course.getScheduleClasses().add(exam.getKey());
    }

    private void linkExamToOtherActivity(OtherActivity otherActivity, Exam exam) {
        exam.setCourse(otherActivity.getKey());
        otherActivity.getScheduleClasses().add(exam.getKey());
    }

    private void linkExamToProfessors(Exam exam) {
        List<String> professorScheduleLinks = new LinkedList<>(exam.getProfessors());

        exam.getProfessors().clear();

        for (String professorScheduleLink : professorScheduleLinks) {
            Professor professor =
                    professorParsingService.getByScheduleLink(professorScheduleLink);

            if (professor != null) {
                professor.getScheduleClasses().add(exam.getKey());
                exam.getProfessors().add(professor.getKey());
            }
            else {
                Log.e("Error finding professor",
                        "Professor schedule link: " + professorScheduleLink);
            }
        }
    }
}
