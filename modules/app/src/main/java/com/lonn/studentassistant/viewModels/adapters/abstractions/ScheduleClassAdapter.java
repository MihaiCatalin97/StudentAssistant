package com.lonn.studentassistant.viewModels.adapters.abstractions;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.ProfessorAdapter;
import com.lonn.studentassistant.viewModels.entities.abstractions.ScheduleClassViewModel;

import java.util.ArrayList;

import static com.lonn.studentassistant.BR._all;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.OTHER_ACTIVITIES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public abstract class ScheduleClassAdapter<T extends ScheduleClass, U extends ScheduleClassViewModel<T>> extends ViewModelAdapter<T, U> {
    public ScheduleClassAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
        super(firebaseConnectedActivity);
    }

    public U adaptOne(U scheduleClassViewModel, T scheduleClass) {
        scheduleClassViewModel
                .setStartHour(scheduleClass.getStartHour())
                .setEndHour(scheduleClass.getEndHour())
                .setGroups(scheduleClass.getGroups())
                .setParity(scheduleClass.getParity())
                .setType(scheduleClass.getType())
                .setRooms(scheduleClass.getRooms())
                .setGroups(scheduleClass.getGroups())
                .setProfessors(new ArrayList<>())
                .setKey(scheduleClass.getKey());

        return scheduleClassViewModel;
    }

    protected U resolveLinks(U viewModel, T scheduleClass) {
        linkProfessors(viewModel, scheduleClass);
        linkCourses(viewModel, scheduleClass);
        linkOtherActivities(viewModel, scheduleClass);

        return viewModel;
    }

    private void linkProfessors(U viewModel, T scheduleClass) {
        ProfessorAdapter professorAdapter = new ProfessorAdapter(this.firebaseConnectedActivity);

        for (String professorId : scheduleClass.getProfessors()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<Professor>()
                            .databaseTable(PROFESSORS)
                            .predicate(where(ID).equalTo(professorId))
                            .onSuccess(professors -> {
                                viewModel.professors.addAll(professorAdapter.adapt(professors, false));
                                viewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }
    }

    private void linkCourses(U viewModel, T scheduleClass) {
        firebaseConnectedActivity.getFirebaseConnection()
                .execute(new GetRequest<Course>()
                        .databaseTable(COURSES)
                        .predicate(where(ID).equalTo(scheduleClass.getDiscipline()))
                        .onSuccess(courses -> {
                            if (courses.size() > 0) {
                                viewModel.setDiscipline(courses.get(0));
                                viewModel.notifyPropertyChanged(_all);
                            }
                        })
                        .subscribe(false));
    }

    private void linkOtherActivities(U viewModel, T scheduleClass) {
        firebaseConnectedActivity.getFirebaseConnection()
                .execute(new GetRequest<OtherActivity>()
                        .databaseTable(OTHER_ACTIVITIES)
                        .predicate(where(ID).equalTo(scheduleClass.getDiscipline()))
                        .onSuccess(otherActivities -> {
                            if (otherActivities.size() > 0) {
                                viewModel.setDiscipline(otherActivities.get(0));
                                viewModel.notifyPropertyChanged(_all);
                            }
                        })
                        .subscribe(false));
    }
}
