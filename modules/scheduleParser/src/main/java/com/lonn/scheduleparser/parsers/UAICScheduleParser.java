package com.lonn.scheduleparser.parsers;

import android.util.Log;

import com.lonn.scheduleparser.repositories.CourseRepository;
import com.lonn.scheduleparser.repositories.OtherActivityRepository;
import com.lonn.scheduleparser.repositories.ProfessorRepository;
import com.lonn.scheduleparser.repositories.ScheduleClassRepository;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.Professor;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import static com.lonn.scheduleparser.parsers.ScheduleLinks.PROFESSORS_PAGE;

public class UAICScheduleParser {
    private static CoursesParser coursesParser;
    private static ProfessorsParser professorsParser;
    private static ScheduleClassesParser scheduleClassesParser;
    private static OtherActivitiesParser professorOtherActivitiesParser;

    private static ScheduleClassRepository scheduleClassRepository;
    private static ProfessorRepository professorRepository;
    private static CourseRepository courseRepository;
    private static OtherActivityRepository otherActivityRepository;

    static {
        coursesParser = new CoursesParser();
        professorsParser = new ProfessorsParser();
        scheduleClassesParser = new ScheduleClassesParser();
        professorOtherActivitiesParser = new OtherActivitiesParser();

        scheduleClassRepository = ScheduleClassRepository.getInstance();
        professorRepository = ProfessorRepository.getInstance();
        courseRepository = CourseRepository.getInstance();
        otherActivityRepository = OtherActivityRepository.getInstance();
    }

    public void parseUAICSchedule() {
        new ParsingThread().start();
    }

    private class ParsingThread extends Thread {
        @Override
        public void run() {
            Document doc = null;

            try {
                doc = Jsoup.connect(PROFESSORS_PAGE).get();
                Log.i("Connected to", PROFESSORS_PAGE);
            } catch (IOException e) {
                if (e.getMessage() != null) {
                    Log.e("Error parsing schedule", e.getMessage());
                }
            }

            if (doc != null) {
                Log.i("Parsing", doc.title());

                List<Professor> parsedProfessors = professorsParser.parse(doc);

                for (Professor parsedProfessor : parsedProfessors) {
                    String professorScheduleLink = parsedProfessor.getScheduleLink();
                    Document professorSchedulePage = readHtmlAtLink(professorScheduleLink);

                    professorRepository.add(parsedProfessor);

                    if (professorSchedulePage != null) {
                        List<Course> parsedCourses =
                                coursesParser.parse(professorSchedulePage);

                        for (Course course : parsedCourses) {
                            course.getProfessors().add(parsedProfessor.getKey());
                        }

                        courseRepository.addAll(parsedCourses);

                        List<OtherActivity> parsedActivities =
                                professorOtherActivitiesParser.parse(professorSchedulePage);

                        for (OtherActivity otherActivity : parsedActivities) {
                            otherActivity.getProfessors().add(parsedProfessor.getKey());
                        }

                        otherActivityRepository.addAll(parsedActivities);

                        List<ScheduleClass> parsedScheduleClasses =
                                scheduleClassesParser.parse(professorSchedulePage);

                        for (ScheduleClass scheduleClass : parsedScheduleClasses) {
                            scheduleClass.getProfessors().add(parsedProfessor.getKey());
                        }

                        scheduleClassRepository.addAll(parsedScheduleClasses);
                    }
                }

                System.out.println("test");
            }
        }

        private Document readHtmlAtLink(String link) {
            Document doc;

            try {
                doc = Jsoup.connect(link).get();
                Log.i("Connected to", link);
            } catch (IOException e) {
                if (e.getMessage() != null) {
                    Log.e("Error parsing schedule", e.getMessage());
                }
                return null;
            }

            return doc;
        }
    }
}
