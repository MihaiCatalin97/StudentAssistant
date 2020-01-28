package com.lonn.studentassistant.debug;

import com.lonn.scheduleparser.ParseResult;
import com.lonn.scheduleparser.UAICScheduleParser;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class DatabasePopulator {
	private static final Logger LOGGER = Logger.ofClass(DatabasePopulator.class);
	private FirebaseConnectedActivity parentActivity;
	private FirebaseApi firebaseApi;

	public DatabasePopulator(FirebaseConnectedActivity parentActivity) {
		this.parentActivity = parentActivity;
		this.firebaseApi = parentActivity.getFirebaseApi();
	}

	public void deleteUsersTable() {
		//
	}

	public void populateStudentsTable() {
		String[] firstNames = new String[]{"Ioana", "Irina", "Raluca", "Andreea", "Catalin", "Marius", "Florin", "Bogdan", "Vlad"};
		String[] lastNames = new String[]{"Tanasuca", "Popescu", "Ionescu", "Mihailescu", "Maftei", "Hurbea", "Pester", "Gorgioaia", "Olariu"};
		Random random = new Random();

		for (int i = 0; i < 10; i++) {
			StudentViewModel student = new StudentViewModel()
					.setStudentId("31090104SL16" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10))
					.setLastName(lastNames[random.nextInt(lastNames.length)])
					.setFirstName(firstNames[random.nextInt(firstNames.length)])
					.setFatherInitial("R")
					.setPhoneNumber("0742664" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10))
					.setGroup((random.nextBoolean() ? "A" : "B") + random.nextInt(7));

			CycleSpecializationYear cycleSpecializationYear = CycleSpecializationYear.values()[random.nextInt(CycleSpecializationYear.values().length)];
			student.setCycleSpecializationYear(cycleSpecializationYear);

			student.setEmail((student.getFirstName() + "." + student.getLastName() + "@email.com").toLowerCase());

			firebaseApi.getStudentService()
					.save(student);
		}
	}

	public void deleteStudentsTable() {
		firebaseApi.getStudentService()
				.deleteAll()
				.onCompleteDoNothing();
	}

	public void deleteCoursesTable() {
		firebaseApi.getCourseService()
				.deleteAll()
				.onCompleteDoNothing();
	}

	public void deleteProfessorsTable() {
		firebaseApi.getProfessorService()
				.deleteAll()
				.onCompleteDoNothing();
	}

	public void populateAdministratorsTable() {
		firebaseApi.getAdministratorService()
				.deleteAll()
				.onCompleteDoNothing();
	}

	public void parseSchedule() {
		UAICScheduleParser uaicScheduleParser = new UAICScheduleParser();

		newSingleThreadExecutor().submit(() -> {
			parentActivity.showSnackBar("Parsing UAIC schedule");

			try {
				ParseResult parseResult = uaicScheduleParser.parseUAICSchedule().get();

				saveCourses(parseResult.getCourses());
				saveProfessors(parseResult.getProfessors());
				saveOtherActivities(parseResult.getOtherActivities());
				saveOneTimeClasses(parseResult.getOneTimeClasses());
				saveRecurringClasses(parseResult.getRecurringClasses());
			}
			catch (InterruptedException | ExecutionException exception) {
				parentActivity.showSnackBar("An error occurred while parsing the schedule!",
						1000);
				LOGGER.error("Failed to parse UAIC schedule", exception);
			}
		});
	}

	private void saveCourses(List<Course> courses) {
		for (Course course : courses) {
			firebaseApi.getCourseService()
					.save(course)
					.onCompleteDoNothing();
		}
	}

	private void saveProfessors(List<Professor> professors) {
		for (Professor professor : professors) {
			firebaseApi.getProfessorService()
					.save(professor)
					.onCompleteDoNothing();
		}
	}

	private void saveOtherActivities(List<OtherActivity> otherActivities) {
		for (OtherActivity otherActivity : otherActivities) {
			firebaseApi.getOtherActivityService()
					.save(otherActivity)
					.onCompleteDoNothing();
		}
	}

	private void saveRecurringClasses(List<RecurringClass> recurringClasses) {
		for (RecurringClass recurringClass : recurringClasses) {
			firebaseApi.getRecurringClassService()
					.save(recurringClass)
					.onCompleteDoNothing();
		}
	}

	private void saveOneTimeClasses(List<OneTimeClass> oneTimeClasses) {
		for (OneTimeClass oneTimeClass : oneTimeClasses) {
			firebaseApi.getOneTimeClassService()
					.save(oneTimeClass)
					.onCompleteDoNothing();
		}
	}
}
