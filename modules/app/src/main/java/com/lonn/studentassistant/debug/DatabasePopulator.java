package com.lonn.studentassistant.debug;

import com.lonn.scheduleparser.ParseResult;
import com.lonn.scheduleparser.UAICScheduleParser;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.common.Logger;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer;
import com.lonn.studentassistant.firebaselayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.DeleteAllRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ADMINISTRATORS;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.STUDENTS;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class DatabasePopulator {
	private static final Logger LOGGER = Logger.ofClass(DatabasePopulator.class);
	private FirebaseConnection firebaseConnection;
	private FirebaseConnectedActivity parentActivity;

	public DatabasePopulator(FirebaseConnection firebaseConnection, FirebaseConnectedActivity parentActivity) {
		this.firebaseConnection = firebaseConnection;
		this.parentActivity = parentActivity;
	}

	public void deleteUsersTable() {
		firebaseConnection.execute(new DeleteAllRequest().databaseTable(DatabaseTableContainer.USERS));
	}

	public void populateStudentsTable() {
		for (int i = 0; i < 10; i++)
			firebaseConnection.execute(new SaveRequest<Student>()
					.entity(new Student()
							.setStudentId(Integer.toString(i))
							.setLastName("Mihai")
							.setFirstName("Catalin")
							.setFatherInitial("R")
							.setEmail("email@email.com")
							.setPhoneNumber("0742664239")
							.setGroup("B5")
							.setYear(3)));
	}

	public void deleteStudentsTable() {
		firebaseConnection.execute(new DeleteAllRequest()
				.databaseTable(STUDENTS));
	}

	public void deleteCoursesTable() {
		firebaseConnection.execute(new DeleteAllRequest()
				.databaseTable(COURSES));
	}

	public void deleteProfessorsTable() {
		firebaseConnection.execute(new DeleteAllRequest()
				.databaseTable(PROFESSORS));
	}

	public void populateAdministratorsTable() {
		Administrator administrator = new Administrator()
				.setFirstName("Catalin")
				.setLastName("Mihai")
				.setAdministratorKey("123");

		firebaseConnection.execute(new SaveRequest<>()
				.databaseTable(ADMINISTRATORS)
				.entity(administrator));
	}

	public void parseSchedule() {
		UAICScheduleParser uaicScheduleParser = new UAICScheduleParser();

		newSingleThreadExecutor().submit(() -> {
			parentActivity.showSnackBar("Parsing UAIC schedule");

			try {
				ParseResult parseResult = uaicScheduleParser.parseUAICSchedule().get();

				saveParsedEntities(parseResult.getCourses(), "courses");
				saveParsedEntities(parseResult.getProfessors(), "professors");
				saveParsedEntities(parseResult.getOtherActivities(), "other activities");
				saveParsedEntities(parseResult.getOneTimeClasses(), "one time classes");
				saveParsedEntities(parseResult.getRecurringClasses(), "recurring classes");
			}
			catch (InterruptedException | ExecutionException exception) {
				parentActivity.showSnackBar("An error occurred while parsing the schedule!",
						1000);
				LOGGER.error("Failed to parse UAIC schedule", exception);
			}
		});
	}

	private <T extends BaseEntity> void saveParsedEntities(List<T> entitiesToSave,
														   String entityName) {
		firebaseConnection.execute(new SaveRequest<T>()
				.entities(entitiesToSave)
				.onSuccess(() ->
						parentActivity.showSnackBar("Saved " + entityName, 1000)
				)
				.onError((Exception exception) -> {
							parentActivity.showSnackBar(
									"An error occurred while saving the " + entityName,
									1000);
							LOGGER.error("Failed saving " + entityName, exception);
						}
				));
	}
}
