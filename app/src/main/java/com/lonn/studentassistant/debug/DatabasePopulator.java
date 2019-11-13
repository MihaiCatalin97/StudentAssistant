package com.lonn.studentassistant.debug;

import com.lonn.studentassistant.debug.parsers.UAICScheduleParser;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.models.Administrator;
import com.lonn.studentassistant.firebaselayer.models.Student;
import com.lonn.studentassistant.firebaselayer.requests.DeleteAllRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;

public class DatabasePopulator {
    private FirebaseConnection firebaseConnection;

    public DatabasePopulator(FirebaseConnection firebaseConnection) {
        this.firebaseConnection = firebaseConnection;
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
                .databaseTable(DatabaseTableContainer.STUDENTS));
    }

    public void deleteCoursesTable() {
        firebaseConnection.execute(new DeleteAllRequest()
                .databaseTable(DatabaseTableContainer.COURSES));
    }

    public void deleteProfessorsTable() {
        firebaseConnection.execute(new DeleteAllRequest()
                .databaseTable(DatabaseTableContainer.PROFESSORS));
    }

    public void populateAdministratorsTable() {
        Administrator administrator = new Administrator()
                .setFirstName("Catalin")
                .setLastName("Mihai")
                .setAdministratorKey("123");

        firebaseConnection.execute(new SaveRequest<>()
                .databaseTable(DatabaseTableContainer.ADMINISTRATORS)
                .entity(administrator));
    }

    public void parseSchedule() {
        UAICScheduleParser uaicScheduleParser = new UAICScheduleParser();

        uaicScheduleParser.parseUAICSchedule();
    }
}
