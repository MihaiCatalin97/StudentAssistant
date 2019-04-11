package com.lonn.studentassistant.activities.student;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.entities.lists.CustomCoursesList;
import com.lonn.studentassistant.entities.lists.CustomStudentList;
import com.lonn.studentassistant.common.abstractClasses.NavBarActivity;
import com.lonn.studentassistant.common.interfaces.ICoursesCallback;
import com.lonn.studentassistant.common.interfaces.IStudentCallback;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.services.coursesService.CourseService;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends NavBarActivity implements IStudentCallback, ICoursesCallback
{
    private CourseAdapter courseAdapter;

    public StudentActivity()
    {
        super(Course.class, Student.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.student_activity_main_layout);
        super.onCreate(savedInstanceState);
        courseAdapter = new CourseAdapter(this, new ArrayList<Course>());
        ((ListView)findViewById(R.id.listCourses)).setAdapter(courseAdapter);
    }

    public void loadCourses(View v)
    {
        showSnackbar("Loading courses");
        courseAdapter.clear();
        ((CourseService) serviceConnections.getServiceByClass(Course.class)).getAll();
    }

    public class CourseAdapter extends ArrayAdapter<Course> {
        CourseAdapter(Context context, List<Course> courses) {
            super(context, 0, courses);
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            Course course = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.course_list_item, parent, false);
            }

            if (course != null) {
                TextView tvName = convertView.findViewById(R.id.textTitlu);
                tvName.setText(course.courseName);
            }

            return convertView;
        }
    }

    public void resultGetAll(CustomCoursesList courses)
    {
        hideSnackbar();
        courseAdapter.addAll(courses);
    }

    public void resultGetById(Course course)
    {

    }

    public void resultGetAll(CustomStudentList students)
    {
        hideSnackbar();
    }

    public void resultGetById(Student student)
    {

    }
}
