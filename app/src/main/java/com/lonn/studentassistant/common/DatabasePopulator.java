package com.lonn.studentassistant.common;

import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.firebaseDatabase.students.StudentDatabaseController;
import com.lonn.studentassistant.firebaseDatabase.students.StudentRepository;

public class DatabasePopulator
{
    StudentRepository studentRepository;

    public DatabasePopulator()
    {
        studentRepository = new StudentRepository(new StudentDatabaseController());
    }

    public void populateStudentsTable()
    {
        studentRepository.add(new Student("1", "Nume", "Prenume", "R", "student@ceva.com", "+406523323", 1, "B5"));
        studentRepository.add(new Student("2", "Mihai", "Marius-Catalin", "R", "mihai.catalin197@gmail.com", "0742664239", 3, "B5"));
        studentRepository.add(new Student("3", "Nume", "Prenume", "R", "student@ceva.com", "+406523323", 1, "B5"));
        studentRepository.add(new Student("4", "Nume", "Prenume", "R", "student@ceva.com", "+406523323", 1, "B5"));
    }
}
