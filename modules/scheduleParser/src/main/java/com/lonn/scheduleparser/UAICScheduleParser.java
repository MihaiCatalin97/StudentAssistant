package com.lonn.scheduleparser;

import android.util.Log;

import com.lonn.scheduleparser.parsingServices.classes.ScheduleClassParsingService;
import com.lonn.scheduleparser.parsingServices.courses.CourseParsingService;
import com.lonn.scheduleparser.parsingServices.exams.ExamParsingService;
import com.lonn.scheduleparser.parsingServices.otherActivities.OtherActivityParsingService;
import com.lonn.scheduleparser.parsingServices.professors.ProfessorParsingService;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.Exam;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.Professor;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;

import java.util.List;

public class UAICScheduleParser {

    private static ProfessorParsingService professorParsingService;
    private static CourseParsingService courseParsingService;
    private static OtherActivityParsingService otherActivityParsingService;
    private static ScheduleClassParsingService scheduleClassParsingService;
    private static ExamParsingService examParsingService;

    static {
        professorParsingService = ProfessorParsingService.getInstance();
        courseParsingService = CourseParsingService.getInstance();
        otherActivityParsingService = OtherActivityParsingService.getInstance();
        scheduleClassParsingService = ScheduleClassParsingService.getInstance();
        examParsingService = ExamParsingService.getInstance();
    }

    public void parseUAICSchedule() {
        professorParsingService.deleteAll();
        courseParsingService.deleteAll();
        otherActivityParsingService.deleteAll();
        scheduleClassParsingService.deleteAll();

        List<Professor> professors = professorParsingService.getAll();
        List<Course> courses = courseParsingService.getAll();
        List<OtherActivity> otherActivities = otherActivityParsingService.getAll();
        List<ScheduleClass> scheduleClasses = scheduleClassParsingService.getAll();
        List<Exam> exams = examParsingService.getAll();

        Log.i("Finished parsing", "All");
    }
}
