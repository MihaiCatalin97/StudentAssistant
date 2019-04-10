package com.lonn.studentassistant.student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.ActivityServicesConnection;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.globalServices.coursesService.CourseService;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity implements IServiceCallback<Course>, NavigationView.OnNavigationItemSelectedListener
{
    private CourseAdapter courseAdapter;
    private ActivityServicesConnection serviceConnections = new ActivityServicesConnection(Course.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_activity_main_layout);
        courseAdapter = new CourseAdapter(this, new ArrayList<Course>());
        ((ListView)findViewById(R.id.listCourses)).setAdapter(courseAdapter);
        initialiseNavBar();
    }

    public void loadCourses(View v)
    {
        courseAdapter.clear();
        ((CourseService) serviceConnections.getServiceByClass(Course.class)).getAll();
    }

    @Override
    public void onStart() {
        super.onStart();

        serviceConnections.bind(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    int count = 0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (count == 0)
                Toast.makeText(getBaseContext(), "Press twice to log out!", Toast.LENGTH_SHORT).show();
            else
            {
                count = 0;
                super.onBackPressed();
            }
        }
        count++;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings: {


                return true;
            }
            case R.id.action_sensors:
            {

                return true;
            }
            case R.id.action_terms:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("These are the terms and conditions")
                        .setTitle("Terms and Conditions");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
            case R.id.action_logout:
            {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        serviceConnections.unbind(this);
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

    public void resultGetAll(List<Course> courses)
    {
        courseAdapter.addAll(courses);
    }

    public void resultGetById(Course course)
    {

    }

    private void initialiseNavBar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
}
