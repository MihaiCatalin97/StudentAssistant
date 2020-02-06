package com.lonn.scheduleparser.parsing.services;

import com.lonn.scheduleparser.parsing.Logger;
import com.lonn.scheduleparser.parsing.abstractions.ParsingService;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.ScheduleClass;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public abstract class ScheduleClassParsingService<T extends ScheduleClass> extends ParsingService<T> {
	private static final Logger LOGGER = Logger.ofClass(ScheduleClassParsingService.class);
	private static ProfessorParsingService professorParsingService;
	private static CourseParsingService courseParsingService;
	private static OtherActivityParsingService otherActivityParsingService;

	static {
		professorParsingService = ProfessorParsingService.getInstance();
		courseParsingService = CourseParsingService.getInstance();
		otherActivityParsingService = OtherActivityParsingService.getInstance();
	}

	protected Future<List<T>> parse() {
		return newSingleThreadExecutor().submit(() -> {
			List<Course> allCourses = courseParsingService.getAll();
			List<OtherActivity> allOtherActivities = otherActivityParsingService.getAll();

			List<T> totalScheduleClasses = parseCoursesScheduleClasses(allCourses);
			totalScheduleClasses.addAll(parseOtherActivitiesScheduleClasses(allOtherActivities));

			repository.addAll(totalScheduleClasses);
			return repository.getAll();
		});
	}


	private List<T> parseCoursesScheduleClasses(List<Course> courses) {
		List<T> parsedEntities = new LinkedList<>();

		for (Course course : courses) {
			List<T> newEntities = parseSinglePage(course.getScheduleLink());

			if (newEntities != null) {
				for (T parsedEntity : newEntities) {
					linkScheduleClassToDiscipline(course, parsedEntity);
					linkScheduleClassAndDisciplineToProfessors(parsedEntity, course);
				}
				parsedEntities.addAll(newEntities);
			}
		}

		return parsedEntities;
	}

	private List<T> parseOtherActivitiesScheduleClasses(List<OtherActivity> otherActivities) {
		List<T> parsedEntities = new LinkedList<>();

		for (OtherActivity otherActivity : otherActivities) {
			List<T> newEntities = parseSinglePage(otherActivity.getScheduleLink());

			if (newEntities != null) {
				for (T parsedEntity : newEntities) {
					linkScheduleClassToDiscipline(otherActivity, parsedEntity);
					linkScheduleClassAndDisciplineToProfessors(parsedEntity, otherActivity);
				}
				parsedEntities.addAll(newEntities);
			}
		}

		return parsedEntities;
	}

	private <U extends Discipline> void linkScheduleClassToDiscipline(U discipline, T scheduleClass) {
		scheduleClass.setDiscipline(discipline.getKey());

		if (scheduleClass instanceof RecurringClass)
			discipline.addRecurringClass(scheduleClass.getKey());
		else if (scheduleClass instanceof OneTimeClass)
			discipline.addOneTimeClass(scheduleClass.getKey());
	}

	private <U extends Discipline> void linkScheduleClassAndDisciplineToProfessors(T scheduleClass, U discipline) {
		List<String> professorScheduleLinks = getProfessorLinksFromScheduleClass(scheduleClass);

		for (String professorScheduleLink : professorScheduleLinks) {
			Professor professor = professorParsingService.getByScheduleLink(professorScheduleLink);

			if (professor != null) {
				linkDisciplineToProfessor(discipline, professor);
				linkScheduleClassToProfessor(scheduleClass, professor);
			}
			else {
				LOGGER.error("Error finding professor with schedule link " + professorScheduleLink);
			}
		}
	}

	private <U extends Discipline> void linkDisciplineToProfessor(U discipline, Professor professor) {
		if (discipline instanceof Course) {
			professor.addCourse(discipline.getKey());
		}
		else {
			professor.addOtherActivity(discipline.getKey());
		}

		discipline.addProfessor(professor.getKey());
	}

	private void linkScheduleClassToProfessor(T scheduleClass, Professor professor) {
		if (scheduleClass instanceof RecurringClass)
			professor.addRecurringClass(scheduleClass.getKey());
		else if (scheduleClass instanceof OneTimeClass)
			professor.addOneTimeClass(scheduleClass.getKey());

		scheduleClass.addProfessor(professor.getKey());
	}

	private List<String> getProfessorLinksFromScheduleClass(T scheduleClass) {
		List<String> professorScheduleLinks = new ArrayList<>(scheduleClass.getProfessors());

		scheduleClass.setProfessors(new LinkedList<>());
		return professorScheduleLinks;
	}
}
