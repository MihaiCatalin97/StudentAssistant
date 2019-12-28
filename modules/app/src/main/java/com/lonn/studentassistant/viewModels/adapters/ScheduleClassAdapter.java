package com.lonn.studentassistant.viewModels.adapters;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.viewModels.entities.ScheduleClassViewModel;

import java.util.ArrayList;

import static com.lonn.studentassistant.BR._all;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.OTHER_ACTIVITIES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;
import static com.lonn.studentassistant.viewModels.entities.ScheduleClassViewModel.builder;

public class ScheduleClassAdapter extends ViewModelAdapter<ScheduleClass, ScheduleClassViewModel> {
    public ScheduleClassAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
        super(firebaseConnectedActivity);
    }

    public ScheduleClassViewModel adaptOne(ScheduleClass scheduleClass) {
        return (ScheduleClassViewModel) builder()
                .startHour(scheduleClass.getStartHour())
                .endHour(scheduleClass.getEndHour())
                .groups(scheduleClass.getGroups())
                .parity(scheduleClass.getParity())
                .type(scheduleClass.getType())
                .rooms(scheduleClass.getRooms())
                .groups(scheduleClass.getGroups())
                .professors(new ArrayList<>())
                .build()
                .setKey(scheduleClass.getKey());
    }

    protected ScheduleClassViewModel resolveLinks(ScheduleClassViewModel viewModel, ScheduleClass scheduleClass) {
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

        firebaseConnectedActivity.getFirebaseConnection()
                .execute(new GetRequest<Course>()
                        .databaseTable(COURSES)
                        .predicate(where(ID).equalTo(scheduleClass.getDiscipline()))
                        .onSuccess(courses -> {
                            if (courses.size() > 0) {
                                viewModel.discipline = courses.get(0);
                                viewModel.notifyPropertyChanged(_all);
                            }
                        })
                        .subscribe(false));

        firebaseConnectedActivity.getFirebaseConnection()
                .execute(new GetRequest<OtherActivity>()
                        .databaseTable(OTHER_ACTIVITIES)
                        .predicate(where(ID).equalTo(scheduleClass.getDiscipline()))
                        .onSuccess(otherActivities -> {
                            if (otherActivities.size() > 0) {
                                viewModel.discipline = otherActivities.get(0);
                                viewModel.notifyPropertyChanged(_all);
                            }
                        })
                        .subscribe(false));

        return viewModel;
    }
}
