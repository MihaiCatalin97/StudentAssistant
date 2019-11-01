package com.lonn.studentassistant.debug;

import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.models.Student;
import com.lonn.studentassistant.firebaselayer.requests.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.requests.DeleteAllRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;

public class DatabasePopulator {
    private FirebaseConnection firebaseConnection;

    public DatabasePopulator(FirebaseConnection firebaseConnection) {
        this.firebaseConnection = firebaseConnection;
    }

    public void deleteUsersTable() {
        firebaseConnection.execute(new DeleteAllRequest().databaseTable(DatabaseTable.USERS));
    }

    public void populateStudentsTable() {
        for(int i=0;i<10;i++)
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
                .databaseTable(DatabaseTable.STUDENTS));
    }

    public void deleteCoursesTable() {
        firebaseConnection.execute(new DeleteAllRequest()
                .databaseTable(DatabaseTable.COURSES));
    }

    public void deleteProfessorsTable() {
        firebaseConnection.execute(new DeleteAllRequest()
                .databaseTable(DatabaseTable.PROFESSORS));
    }
//
//    public void parseSchedule() {
//        URLParser conn = new URLParser("https://profs.info.uaic.ro/~orar/orar_profesori.html",
//                firebaseConnection);
//        conn.parse();
//    }
}
