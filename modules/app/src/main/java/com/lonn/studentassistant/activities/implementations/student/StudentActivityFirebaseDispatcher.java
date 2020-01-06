package com.lonn.studentassistant.activities.implementations.student;

import android.util.Log;

import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.CourseAdapter;
import com.lonn.studentassistant.viewModels.adapters.OneTimeClassAdapter;
import com.lonn.studentassistant.viewModels.adapters.OtherActivityAdapter;
import com.lonn.studentassistant.viewModels.adapters.ProfessorAdapter;
import com.lonn.studentassistant.viewModels.adapters.RecurringClassAdapter;
import com.lonn.studentassistant.viewModels.entities.OneTimeClassViewModel;
import com.lonn.studentassistant.viewModels.entities.RecurringClassViewModel;
import com.lonn.studentassistant.viewModels.entities.StudentViewModel;

import java.util.HashMap;
import java.util.Map;

import static com.lonn.studentassistant.BR.courses;
import static com.lonn.studentassistant.BR.oneTimeClasses;
import static com.lonn.studentassistant.BR.otherActivities;
import static com.lonn.studentassistant.BR.professors;
import static com.lonn.studentassistant.BR.recurringClasses;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ONE_TIME_CLASSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.OTHER_ACTIVITIES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.RECURRING_CLASSES;

class StudentActivityFirebaseDispatcher {
    private StudentActivityMainLayoutBinding binding;
    private FirebaseConnection firebaseConnection;
    private StudentActivity studentActivity;

    private RecurringClassAdapter recurringClassAdapter;
    private OneTimeClassAdapter oneTimeClassAdapter;

    private CourseAdapter courseAdapter;
    private ProfessorAdapter professorAdapter;
    private OtherActivityAdapter otherActivityAdapter;

    StudentActivityFirebaseDispatcher(StudentActivity studentActivity) {
        this.studentActivity = studentActivity;
        binding = studentActivity.binding;
        firebaseConnection = studentActivity.getFirebaseConnection();

        recurringClassAdapter = new RecurringClassAdapter(studentActivity);
        oneTimeClassAdapter = new OneTimeClassAdapter(studentActivity);
        courseAdapter = new CourseAdapter(studentActivity);
        professorAdapter = new ProfessorAdapter(studentActivity);
        otherActivityAdapter = new OtherActivityAdapter(studentActivity);
    }

    void loadCourses() {
        if (binding.getCourses() == null) {
            firebaseConnection.execute(new GetRequest<Course>()
                    .databaseTable(COURSES)
                    .onSuccess(receivedCourses -> {
                        binding.setCourses(courseAdapter.adapt(receivedCourses));
                        binding.notifyPropertyChanged(courses);
                    })
                    .onError(error -> {
                        Log.e("Loading courses", error.getMessage());
                        studentActivity.showSnackBar("An error occurred while loading the courses.");
                    }));
        }
    }

    void loadOtherActivities() {
        if (binding.getOtherActivities() == null) {
            firebaseConnection.execute(new GetRequest<OtherActivity>()
                    .databaseTable(OTHER_ACTIVITIES)
                    .onSuccess(receivedEntities -> {
                        binding.setOtherActivities(otherActivityAdapter.adapt(receivedEntities));
                        binding.notifyPropertyChanged(otherActivities);
                    })
                    .onError(error -> {
                        Log.e("Loading activities", error.getMessage());
                        studentActivity.showSnackBar("An error occurred while loading activities.");
                    }));
        }
    }

    void loadProfessors() {
        if (binding.getProfessors() == null) {
            firebaseConnection.execute(new GetRequest<Professor>()
                    .databaseTable(PROFESSORS)
                    .onSuccess(receivedProfessors -> {
                        binding.setProfessors(professorAdapter.adapt(receivedProfessors));
                        binding.notifyPropertyChanged(professors);
                    })
                    .onError(error -> {
                        Log.e("Loading professors", error.getMessage());
                        studentActivity.showSnackBar("An error occurred while loading the professors.");
                    }));
        }
    }

    void loadRecurringClasses(StudentViewModel studentViewModel) {
        if (binding.getRecurringClasses() == null) {
            firebaseConnection.execute(new GetRequest<RecurringClass>()
                    .databaseTable(RECURRING_CLASSES)
                    .onSuccess(receivedRecurringClasses -> {
                        Map<String, RecurringClassViewModel> recurringClassViewModels = new HashMap<>();

                        for (RecurringClass recurringClass : receivedRecurringClasses) {
                            if (isPersonalScheduleClass(recurringClass, studentViewModel)) {
                                recurringClassViewModels.put(recurringClass.getKey(),
                                        recurringClassAdapter.adapt(recurringClass));
                            }
                        }

                        binding.setRecurringClasses(recurringClassViewModels);
                        binding.notifyPropertyChanged(recurringClasses);
                    })
                    .onError(error -> {
                        Log.e("Loading R. classes", error.getMessage());
                        studentActivity.showSnackBar("An error occurred while loading the recurring classes.");
                    }));
        }
    }

    void loadOneTimeClasses(StudentViewModel studentViewModel) {
        if (binding.getOneTimeClasses() == null) {
            firebaseConnection.execute(new GetRequest<OneTimeClass>()
                    .databaseTable(ONE_TIME_CLASSES)
                    .onSuccess(receivedOneTimeClasses ->
                    {
                        Map<String, OneTimeClassViewModel> oneTimeClassViewModels = new HashMap<>();

                        for (OneTimeClass oneTimeClass : receivedOneTimeClasses) {
                            if (isPersonalScheduleClass(oneTimeClass, studentViewModel)) {
                                oneTimeClassViewModels.put(oneTimeClass.getKey(),
                                        oneTimeClassAdapter.adapt(oneTimeClass));
                            }
                        }

                        binding.setOneTimeClasses(oneTimeClassViewModels);
                        binding.notifyPropertyChanged(oneTimeClasses);
                    })
                    .onError(error ->

                    {
                        Log.e("Loading OT. classes", error.getMessage());
                        studentActivity.showSnackBar("An error occurred while loading the one time classes.");
                    }));
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
