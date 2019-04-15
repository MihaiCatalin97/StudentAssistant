package com.lonn.studentassistant.activities.student;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.requests.Request;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;
import com.lonn.studentassistant.common.responses.Response;
import com.lonn.studentassistant.common.abstractClasses.NavBarActivity;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.services.coursesService.CourseService;
import com.lonn.studentassistant.services.studentService.StudentService;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends NavBarActivity implements IServiceCallback
{
    private CourseAdapter courseAdapter;

    public StudentActivity()
    {
        super(CourseService.class, StudentService.class);
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
        serviceConnections.getServiceByClass(CourseService.class).postRequest(new Request("getAll"));
    }

    public void processResponse(Response response)
    {
        hideSnackbar();

        if(!response.result.equals("success"))
            showSnackbar("Fail");

        Log.e("Received response " + response.type.getName(), response.result + " " + response.action);
        if(response.type.equals(Course.class))
        {
            Log.e("Received response " + response.type.getName(), response.result + " " + response.action);
            if(response.action.equals("getAll"))
            {
                Log.e("Received response " + response.type.getName(), response.result + " " + response.action + " " + response.items.size());
                courseAdapter.addAll(response.items);
            }
        }
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
}
