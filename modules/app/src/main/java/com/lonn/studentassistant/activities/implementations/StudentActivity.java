package com.lonn.studentassistant.activities.implementations;

import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.Utils;
import com.lonn.studentassistant.activities.abstractions.NavBarActivity;
import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.CourseAdapter;
import com.lonn.studentassistant.viewModels.adapters.ProfessorAdapter;
import com.lonn.studentassistant.viewModels.entities.CourseViewModel;
import com.lonn.studentassistant.viewModels.entities.OtherActivityViewModel;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;
import com.lonn.studentassistant.viewModels.entities.ScheduleClassViewModel;
import com.lonn.studentassistant.viewModels.entities.StudentViewModel;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;

import static android.view.View.VISIBLE;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;

public class StudentActivity extends NavBarActivity {
    public ScrollViewCategory<CourseViewModel> coursesBaseCategory;
    public ScrollViewCategory<ProfessorViewModel> professorsBaseCategory;
    public ScrollViewCategory<OtherActivityViewModel> otherActivitiesBaseCategory;
    public ScrollViewCategory<ScheduleClassViewModel> scheduleClassBaseCategory;
    public ScrollViewCategory<CourseViewModel> coursesProfileCategory;
    public ScrollViewCategory<OtherActivityViewModel> otherActivitiesProfileCategory;
    private StudentViewModel studentViewModel;
    private StudentActivityMainLayoutBinding binding;

    private CourseAdapter courseAdapter = new CourseAdapter(this);
    private ProfessorAdapter professorAdapter = new ProfessorAdapter(this);

    public void updateEntity(Student student) {
        if (studentViewModel == null) {
            studentViewModel = new StudentViewModel(student);
            binding.setStudent(studentViewModel);
        }
        else {
            studentViewModel.update(student);
        }
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.layoutHome).getVisibility() == VISIBLE) {
            super.onBackPressed();
        }
        else {
            handleNavBarAction(R.id.nav_home);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseConnection.execute(new GetRequest<Course>()
                .databaseTable(COURSES)
                .onSuccess(courses -> {
                    binding.setCourses(courseAdapter.adapt(courses));
                    binding.notifyPropertyChanged(com.lonn.studentassistant.BR.courses);
                })
                .onError(error -> {
                    Log.e("Loading courses", error.getMessage());
                    showSnackBar("An error occurred while loading the courses.");
                }));

        firebaseConnection.execute(new GetRequest<Professor>()
                .databaseTable(PROFESSORS)
                .onSuccess(professors -> {
                    binding.setProfessors(professorAdapter.adapt(professors));
                    binding.notifyPropertyChanged(com.lonn.studentassistant.BR.professors);
                })
                .onError(error -> {
                    Log.e("Loading professors", error.getMessage());
                    showSnackBar("An error occurred while loading the professors.");
                }));
    }

    public void handleNavBarAction(int id) {
        if (id == R.id.nav_home) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutHome).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_profile) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutProfile).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_schedule) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutSchedule).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_grades) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutGrades).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_messages) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutMessages).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_professors) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutProfessors).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_courses) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutCourses).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_administrative) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutAdministrative).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_otherActivities) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutOtherActivities).setVisibility(VISIBLE);
        }
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.student_activity_main_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        coursesBaseCategory = findViewById(R.id.coursesBaseCategory);
        professorsBaseCategory = findViewById(R.id.professorsBaseCategory);
        otherActivitiesBaseCategory = findViewById(R.id.otherActivitiesBaseCategory);
        scheduleClassBaseCategory = findViewById(R.id.scheduleBaseCategory);

        ScrollView profilePage = findViewById(R.id.layoutProfile);
        coursesProfileCategory = profilePage.findViewById(R.id.coursesProfileCategory);
        otherActivitiesProfileCategory = profilePage.findViewById(R.id.otherActivitiesProfileCategory);
    }
}
