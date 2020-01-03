package com.lonn.studentassistant.activities.implementations;

import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.Utils;
import com.lonn.studentassistant.activities.abstractions.NavBarActivity;
import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.CourseAdapter;
import com.lonn.studentassistant.viewModels.adapters.OneTimeClassAdapter;
import com.lonn.studentassistant.viewModels.adapters.ProfessorAdapter;
import com.lonn.studentassistant.viewModels.adapters.RecurringClassAdapter;
import com.lonn.studentassistant.viewModels.adapters.StudentAdapter;
import com.lonn.studentassistant.viewModels.entities.OneTimeClassViewModel;
import com.lonn.studentassistant.viewModels.entities.RecurringClassViewModel;
import com.lonn.studentassistant.viewModels.entities.StudentViewModel;

import java.util.LinkedList;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.BR.recurringClasses;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ONE_TIME_CLASSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.RECURRING_CLASSES;

public class StudentActivity extends NavBarActivity {
    private StudentViewModel studentViewModel;
    private StudentActivityMainLayoutBinding binding;

    private CourseAdapter courseAdapter = new CourseAdapter(this);
    private ProfessorAdapter professorAdapter = new ProfessorAdapter(this);
    private RecurringClassAdapter recurringClassAdapter = new RecurringClassAdapter(this);
    private OneTimeClassAdapter oneTimeClassAdapter = new OneTimeClassAdapter(this);

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
                .onSuccess(receivedCourses -> {
                    binding.setCourses(courseAdapter.adapt(receivedCourses));
                    binding.notifyPropertyChanged(courses);
                })
                .onError(error -> {
                    Log.e("Loading optionalCourses", error.getMessage());
                    showSnackBar("An error occurred while loading the optionalCourses.");
                }));

        firebaseConnection.execute(new GetRequest<Professor>()
                .databaseTable(PROFESSORS)
                .onSuccess(receivedProfessors -> {
                    binding.setProfessors(professorAdapter.adapt(receivedProfessors));
                    binding.notifyPropertyChanged(professors);
                })
                .onError(error -> {
                    Log.e("Loading professors", error.getMessage());
                    showSnackBar("An error occurred while loading the professors.");
                }));

        firebaseConnection.execute(new GetRequest<RecurringClass>()
                .databaseTable(RECURRING_CLASSES)
                .onSuccess(receivedRecurringClasses -> {
                    List<RecurringClassViewModel> recurringClassViewModels = new LinkedList<>();

                    for (RecurringClass recurringClass : receivedRecurringClasses) {
                        if (isPersonalScheduleClass(recurringClass, studentViewModel)) {
                            recurringClassViewModels.add(recurringClassAdapter.adapt(recurringClass));
                        }
                    }

                    binding.setRecurringClasses(recurringClassViewModels);
                    binding.notifyPropertyChanged(recurringClasses);
                })
                .onError(error -> {
                    Log.e("Loading R. classes", error.getMessage());
                    showSnackBar("An error occurred while loading the recurring classes.");
                }));

        firebaseConnection.execute(new GetRequest<OneTimeClass>()
                .databaseTable(ONE_TIME_CLASSES)
                .onSuccess(receivedOneTimeClasses -> {
                    List<OneTimeClassViewModel> oneTimeClassViewModels = new LinkedList<>();

                    for (OneTimeClass oneTimeClass : receivedOneTimeClasses) {
                        if (isPersonalScheduleClass(oneTimeClass, studentViewModel)) {
                            oneTimeClassViewModels.add(oneTimeClassAdapter.adapt(oneTimeClass));
                        }
                    }

                    binding.setOneTimeClasses(oneTimeClassViewModels);
                    binding.notifyPropertyChanged(oneTimeClasses);
                })
                .onError(error -> {
                    Log.e("Loading OT. classes", error.getMessage());
                    showSnackBar("An error occurred while loading the one time classes.");
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

        if (getIntent().getExtras() != null) {
            Student student = (Student) getIntent().getExtras().get("student");

            if (student != null) {
                studentViewModel = new StudentAdapter(this).adaptOne(student);
            }
        }
    }

    private boolean isPersonalScheduleClass(ScheduleClass scheduleClass,
                                            StudentViewModel studentViewModel) {
        String year = studentViewModel.getCycleSpecialization()
                .getInitials() + studentViewModel.getYear();

        String semiYear = year + studentViewModel.getGroup().charAt(0);
        String group = year + studentViewModel.getGroup();

        return scheduleClass.getGroups().contains(year) ||
                scheduleClass.getGroups().contains(semiYear) ||
                scheduleClass.getGroups().contains(group);
    }
}
