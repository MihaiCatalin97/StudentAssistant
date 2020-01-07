package com.lonn.studentassistant.viewModels.adapters.abstractions;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.viewModels.adapters.FileMetadataAdapter;
import com.lonn.studentassistant.viewModels.adapters.OneTimeClassAdapter;
import com.lonn.studentassistant.viewModels.adapters.ProfessorAdapter;
import com.lonn.studentassistant.viewModels.adapters.RecurringClassAdapter;
import com.lonn.studentassistant.viewModels.entities.abstractions.DisciplineViewModel;

import java.util.ArrayList;

import static com.lonn.studentassistant.BR._all;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_METADATA;
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
                .setOneTimeClasses(new ArrayList<>())
                .setRecurringClasses(new ArrayList<>())
                .setKey(discipline.getKey());

        return disciplineViewModel;
    }

    protected U resolveLinks(U disciplineViewModel, T discipline) {
        linkProfessors(disciplineViewModel, discipline);
        linkRecurringClasses(disciplineViewModel, discipline);
        linkOneTimeClasses(disciplineViewModel, discipline);
        linkFiles(disciplineViewModel, discipline);

        return disciplineViewModel;
    }

    private void linkFiles(U disciplineViewModel, T discipline) {
        FileMetadataAdapter fileMetadataAdapter = new FileMetadataAdapter(this.firebaseConnectedActivity);

        for (String fileMetadataId : discipline.getFilesMetadata()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<FileMetadata>()
                            .databaseTable(FILE_METADATA)
                            .predicate(where(ID)
                                    .equalTo(fileMetadataId))
                            .onSuccess(files -> {
                                disciplineViewModel.filesMetadata.addAll(fileMetadataAdapter.adapt(files, false));
                                disciplineViewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }
    }

    private void linkProfessors(U disciplineViewModel, T discipline) {
        ProfessorAdapter professorAdapter = new ProfessorAdapter(this.firebaseConnectedActivity);

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
    }

    private void linkOneTimeClasses(U disciplineViewModel, T discipline) {
        OneTimeClassAdapter oneTimeClassAdapter = new OneTimeClassAdapter(this.firebaseConnectedActivity);

        for (String scheduleClassId : discipline.getScheduleClasses()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<OneTimeClass>()
                            .databaseTable(ONE_TIME_CLASSES)
                            .predicate(where(ID)
                                    .equalTo(scheduleClassId))
                            .onSuccess(oneTimeClasses -> {
                                disciplineViewModel.oneTimeClasses.addAll(oneTimeClassAdapter.adapt(oneTimeClasses));
                                disciplineViewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }
    }

    private void linkRecurringClasses(U disciplineViewModel, T discipline) {
        RecurringClassAdapter recurringClassAdapter = new RecurringClassAdapter(this.firebaseConnectedActivity);

        for (String scheduleClassId : discipline.getScheduleClasses()) {
            firebaseConnectedActivity.getFirebaseConnection()
                    .execute(new GetRequest<RecurringClass>()
                            .databaseTable(RECURRING_CLASSES)
                            .predicate(where(ID)
                                    .equalTo(scheduleClassId))
                            .onSuccess(recurringClasses -> {
                                disciplineViewModel.recurringClasses.addAll(recurringClassAdapter.adapt(recurringClasses));
                                disciplineViewModel.notifyPropertyChanged(_all);
                            })
                            .subscribe(false));
        }
    }
}