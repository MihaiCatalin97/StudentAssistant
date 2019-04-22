package com.lonn.studentassistant.activities.implementations.student;

import android.content.Context;
import android.content.Intent;
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
import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.requests.GetAllRequest;
import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.activities.abstractions.NavBarActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.services.implementations.coursesService.CourseService;
import com.lonn.studentassistant.services.implementations.studentService.StudentService;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends NavBarActivity
{
    private CourseAdapter courseAdapter;

    public StudentActivity()
    {
        super(CourseService.class);
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
        ((CourseService)serviceConnections.getServiceByClass(CourseService.class)).postRequest(new GetAllRequest<Course>());
    }

    @Override
    public void onStop()
    {
        super.onStop();
        serviceConnections.unbind(courseCallback, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        serviceConnections.bind(CourseService.class, courseCallback, this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    public void handleNavBarAction(int id)
    {
        if (id == R.id.nav_schedule) {
            // Handle the camera action
        } else if (id == R.id.nav_grades) {

        } else if (id == R.id.nav_messages) {

        } else if (id == R.id.nav_professors) {

        } else if (id == R.id.nav_courses) {

        } else if (id == R.id.nav_administrative) {

        }
    }

    private IDatabaseCallback<Course> courseCallback = new IDatabaseCallback<Course>()
    {
        public void processResponse(DatabaseResponse<Course> res)
        {
            Log.e("Default response","Received");
        }

        public void processResponse(CreateResponse<Course> response)
        {

        }

        public void processResponse(DeleteResponse<Course> response)
        {

        }

        public void processResponse(EditResponse<Course> response)
        {

        }

        public void processResponse(GetByIdResponse<Course> response)
        {

        }

        public void processResponse(GetAllResponse<Course> response)
        {
            hideSnackbar();
            Log.e("Received responseCourse", response.getResult() + " " + response.getAction() + " " + response.getItems().size());

            if(!response.getResult().equals("success"))
                showSnackbar("Fail");
            else
            {
                if (response.getAction().equals("getAll"))
                {
                    courseAdapter.addAll(response.getItems());
                }
            }
        }
    };
}
