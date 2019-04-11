package com.lonn.studentassistant.common;

import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.entities.lists.CustomCoursesList;
import com.lonn.studentassistant.entities.lists.CustomStudentList;
import com.lonn.studentassistant.services.studentService.dataAccessLayer.StudentDatabaseController;
import com.lonn.studentassistant.services.studentService.dataAccessLayer.StudentRepository;
import com.lonn.studentassistant.services.userService.dataAccessLayer.UserDatabaseController;
import com.lonn.studentassistant.services.userService.dataAccessLayer.UserRepository;
import com.lonn.studentassistant.services.coursesService.dataAccessLayer.CourseDatabaseController;
import com.lonn.studentassistant.services.coursesService.dataAccessLayer.CourseRepository;

import java.util.ArrayList;
import java.util.List;

public class DatabasePopulator
{
    private StudentRepository studentRepository;
    private UserRepository userRepository;
    private CourseRepository courseRepository;

    public DatabasePopulator()
    {
        studentRepository = new StudentRepository(new StudentDatabaseController());
        userRepository = new UserRepository(new UserDatabaseController());
        courseRepository = new CourseRepository(new CourseDatabaseController(null));
    }

    public void deleteStudentsTable()
    {
        CustomStudentList students = (CustomStudentList) studentRepository.getAll();

        studentRepository.remove(students);
    }

    public void deleteUsersTable()
    {
        List<User> users = userRepository.getAll();

        for(int i=0;i<users.size();i++)
        {
            userRepository.remove(users.get(i));
        }
    }

    public void populateStudentsTable()
    {
        CustomStudentList newStudents = new CustomStudentList();

        newStudents.add(new Student("1", "Mihai", "Catalin", "R", "cmihai@gmail.com", "0742664239", 3, "B5"));
        newStudents.add(new Student("2", "Tanasuca", "Bogdan", "R", "btanasuca@gmail.com", "0742664239", 3, "B5"));
        newStudents.add(new Student("3", "Cretu", "Marius", "R", "mcretu@gmail.com", "0742664239", 3, "B5"));
        newStudents.add(new Student("4", "Borceanu", "Florin", "R", "bflorin@gmail.com", "0742664239", 3, "B5"));
        newStudents.add(new Student("5", "Andro", "Bianca", "R", "bandro@gmail.com", "0742664239", 3, "B5"));
        newStudents.add(new Student("6", "Hurbea", "Razvan", "R", "rhurbea@gmail.com", "0742664239", 3, "A1"));

        studentRepository.add(newStudents);
    }

    public void populateCoursesTable()
    {
        CustomCoursesList newCourses = new CustomCoursesList();

        newCourses.add(new Course("Java", 2, 2));
        newCourses.add(new Course("TSP.Net", 3, 2));
        newCourses.add(new Course("Securitatea Informatiei", 3, 1));
        newCourses.add(new Course("Logica", 1, 1));
        newCourses.add(new Course("POO", 1, 2));

        courseRepository.add(newCourses);
    }
}
