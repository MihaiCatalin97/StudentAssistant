package com.lonn.studentassistant.debug;

import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.services.implementations.gradeService.dataAccessLayer.GradeRepository;
import com.lonn.studentassistant.services.implementations.professorService.dataAccessLayer.ProfessorRepository;
import com.lonn.studentassistant.services.implementations.studentService.dataAccessLayer.StudentRepository;
import com.lonn.studentassistant.services.implementations.userService.dataAccessLayer.UserRepository;
import com.lonn.studentassistant.services.implementations.coursesService.dataAccessLayer.CourseRepository;

import java.util.ArrayList;
import java.util.List;

public class DatabasePopulator
{
    private StudentRepository studentRepository;
    private UserRepository userRepository;
    private CourseRepository courseRepository;
    private GradeRepository gradeRepository;
    private ProfessorRepository professorRepository;

    public DatabasePopulator()
    {
        studentRepository = StudentRepository.getInstance(null);
        userRepository = UserRepository.getInstance(null);
        courseRepository = CourseRepository.getInstance(null);
        gradeRepository = GradeRepository.getInstance(null);
        professorRepository = ProfessorRepository.getInstance(null);
    }

    public void deleteUsersTable()
    {
        userRepository.remove(userRepository.getAll());
    }

    public void populateStudentsTable()
    {
        List<Student> newStudents = new ArrayList<>();

        newStudents.add(new Student("1", "Mihai", "Catalin", "R", "cmihai@gmail.com", "0742664239", 3, "B5"));
        newStudents.add(new Student("2", "Tanasuca", "Bogdan", "R", "btanasuca@gmail.com", "0742664239", 3, "B5"));
        newStudents.add(new Student("3", "Cretu", "Marius", "R", "mcretu@gmail.com", "0742664239", 3, "B5"));
        newStudents.add(new Student("4", "Borceanu", "Florin", "R", "bflorin@gmail.com", "0742664239", 3, "B5"));
        newStudents.add(new Student("5", "Andro", "Bianca", "R", "bandro@gmail.com", "0742664239", 3, "B5"));
        newStudents.add(new Student("6", "Hurbea", "Razvan", "R", "rhurbea@gmail.com", "0742664239", 3, "A1"));

        studentRepository.add(newStudents);
    }

    public void deleteStudentsTable()
    {
        studentRepository.remove(studentRepository.getAll());
    }

    public void deleteCoursesTable()
    {
        courseRepository.remove(courseRepository.getAll());
    }

    public void deleteProfessorsTable()
    {
        professorRepository.remove(professorRepository.getAll());
    }

    public void populateGradesTable()
    {

    }

    public void parseSchedule()
    {
        URLParser conn = new URLParser("https://profs.info.uaic.ro/~orar/orar_profesori.html");
        conn.parse();
    }
}
