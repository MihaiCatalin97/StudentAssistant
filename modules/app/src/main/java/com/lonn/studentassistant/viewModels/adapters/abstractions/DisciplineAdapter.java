package com.lonn.studentassistant.viewModels.adapters.abstractions;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.OneTimeClassAdapter;
import com.lonn.studentassistant.viewModels.adapters.ProfessorAdapter;
import com.lonn.studentassistant.viewModels.adapters.RecurringClassAdapter;
import com.lonn.studentassistant.viewModels.entities.abstractions.DisciplineViewModel;

import java.util.ArrayList;

import static com.lonn.studentassistant.BR._all;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ONE_TIME_CLASSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.RECURRING_CLASSES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public abstract class DisciplineAdapter<T extends Discipline, U extends DisciplineViewModel<T>> extends ViewModelAdapter<T, U> {
    protected DisciplineAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
        super(firebaseConnectedActivity);
    }

    protected U adaptOne(U disciplineViewModel, T discipline) {
        disciplineViewModel.setName(discipline.getDisciplineName())
                .setDescription(discipline.getDescription())
                .setWebsite(discipline.getWebsite())
                .setYear(discipline.getYear())
                .setSemester(discipline.getSemester())
                .setProfessors(new ArrayList<>())
                .setScheduleClasses(new ArrayList<>())
                .setKey(discipline.getKey());

        return disciplineViewModel;
    }

    protected U resolveLinks(U disciplineViewModel, T discipline) {
        ProfessorAdapter professorAdapter = new ProfessorAdapter(this.firebaseConnectedActivity);
        RecurringClassAdapter recurringClassAdapter = new RecurringClassAdapter(this.firebaseConnectedActivity);
        OneTimeClassAdapter oneTimeClassAdapter = new OneTimeClassAdapter(this.firebaseConnectedActivity);

        for (String professorId : discipline.getProfessors()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<Professor>()
                            .databaseTable(PROFESSORS)
                            .predicate(where(ID)
                                    .equalTo(professorId))
                            .onSuccess(professors -> {
                                disciplineViewModel.professors.addAll(professorAdapter.adapt(professors, false));
                                disciplineViewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }

        for (String scheduleClassId : discipline.getScheduleClasses()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<RecurringClass>()
                            .databaseTable(RECURRING_CLASSES)
                            .predicate(where(ID)
                                    .equalTo(scheduleClassId))
                            .onSuccess(recurringClasses -> {
                                disciplineViewModel.scheduleClasses.addAll(recurringClassAdapter.adapt(recurringClasses, false));
                                disciplineViewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }

        for (String scheduleClassId : discipline.getScheduleClasses()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<OneTimeClass>()
                            .databaseTable(ONE_TIME_CLASSES)
                            .predicate(where(ID)
                                    .equalTo(scheduleClassId))
                            .onSuccess(oneTimeClasses -> {
                                disciplineViewModel.scheduleClasses.addAll(oneTimeClassAdapter.adapt(oneTimeClasses, false));
                                disciplineViewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }

        return disciplineViewModel;
    }
}
