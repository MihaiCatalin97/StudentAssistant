package com.lonn.studentassistant.viewModels.adapters;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;

import java.util.ArrayList;

import static com.lonn.studentassistant.BR._all;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ONE_TIME_CLASSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.OTHER_ACTIVITIES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.RECURRING_CLASSES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;
import static com.lonn.studentassistant.viewModels.entities.ProfessorViewModel.builder;

public class ProfessorAdapter extends ViewModelAdapter<Professor, ProfessorViewModel> {
    public ProfessorAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
        super(firebaseConnectedActivity);
    }

    public ProfessorViewModel adaptOne(Professor professor) {
        return (ProfessorViewModel) builder()
                .firstName(professor.getFirstName())
                .lastName(professor.getLastName())
                .cabinet(professor.getCabinet())
                .email(professor.getEmail())
                .phoneNumber(professor.getPhoneNumber())
                .professorImage(professor.getProfessorImageMetadataKey())
                .rank(professor.getLevel())
                .website(professor.getWebsite())
                .courses(new ArrayList<>())
                .otherActivities(new ArrayList<>())
                .oneTimeClasses(new ArrayList<>())
                .recurringClasses(new ArrayList<>())
                .build()
                .setKey(professor.getKey());
    }

    protected ProfessorViewModel resolveLinks(ProfessorViewModel professorViewModel, Professor professor) {
        linkCourses(professorViewModel, professor);
        linkOtherActivities(professorViewModel, professor);
        linkRecurringClasses(professorViewModel, professor);
        linkOneTimeClasses(professorViewModel, professor);

        return professorViewModel;
    }

    private void linkCourses(ProfessorViewModel professorViewModel, Professor professor) {
        CourseAdapter courseAdapter = new CourseAdapter(firebaseConnectedActivity);

        for (String courseId : professor.getCourses()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<Course>()
                            .databaseTable(COURSES)
                            .predicate(where(ID).equalTo(courseId))
                            .onSuccess(courses -> {
                                professorViewModel.courses.addAll(courseAdapter.adapt(courses, false));
                                professorViewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }
    }

    private void linkOtherActivities(ProfessorViewModel professorViewModel, Professor professor) {
        OtherActivityAdapter otherActivityAdapter = new OtherActivityAdapter(firebaseConnectedActivity);

        for (String otherActivityId : professor.getOtherActivities()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<OtherActivity>()
                            .databaseTable(OTHER_ACTIVITIES)
                            .predicate(where(ID).equalTo(otherActivityId))
                            .onSuccess(otherActivities -> {
                                professorViewModel.otherActivities.addAll(otherActivityAdapter.adapt(otherActivities, false));
                                professorViewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }
    }

    private void linkRecurringClasses(ProfessorViewModel professorViewModel, Professor professor) {
        RecurringClassAdapter recurringClassAdapter = new RecurringClassAdapter(firebaseConnectedActivity);

        for (String scheduleClassId : professor.getScheduleClasses()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<RecurringClass>()
                            .databaseTable(RECURRING_CLASSES)
                            .predicate(where(ID).equalTo(scheduleClassId))
                            .onSuccess(recurringClasses -> {
                                professorViewModel.recurringClasses.addAll(recurringClassAdapter.adapt(recurringClasses));
                                professorViewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }
    }

    private void linkOneTimeClasses(ProfessorViewModel professorViewModel, Professor professor) {
        OneTimeClassAdapter oneTimeClassAdapter = new OneTimeClassAdapter(firebaseConnectedActivity);

        for (String scheduleClassId : professor.getScheduleClasses()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<OneTimeClass>()
                            .databaseTable(ONE_TIME_CLASSES)
                            .predicate(where(ID).equalTo(scheduleClassId))
                            .onSuccess(oneTimeClasses -> {
                                professorViewModel.oneTimeClasses.addAll(oneTimeClassAdapter.adapt(oneTimeClasses));
                                professorViewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }
    }
}
