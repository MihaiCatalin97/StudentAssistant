package com.lonn.studentassistant.entities.lists;

import com.lonn.studentassistant.entities.Student;

import java.util.List;

public class CustomStudentList extends CustomList<Student>
{
    public CustomStudentList() {super();}
    public CustomStudentList(List<Student> students)
    {
        super(students);
    }
}
